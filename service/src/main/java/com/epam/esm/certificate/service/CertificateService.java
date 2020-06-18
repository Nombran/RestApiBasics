package com.epam.esm.certificate.service;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.dto.CertificateDto;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.certificate.specification.CertificateSearchSqlBuilder;
import com.epam.esm.certificatetag.dao.CertificateTagDao;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateService {

    private final CertificateDao certificateDao;

    private final TagDao tagDao;

    private final CertificateTagDao certificateTagDao;

    private final ModelMapper modelMapper;

    public CertificateService(TagDao tagDao,
                              CertificateDao certificateDao,
                              ModelMapper modelMapper,
                              CertificateTagDao certificateTagDao) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
        this.modelMapper = modelMapper;
        this.certificateTagDao = certificateTagDao;
    }

    public void create(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        certificateDao.create(certificate);
        List<String> tags = certificateDto.getTags();
        addCertificateTags(tags, certificate.getId());
    }

    public void update(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        long certificateId = certificate.getId();
        certificateDao.update(certificate);
        List<String> tags = certificateDto.getTags();
        updateCertificateTags(tags, certificateId);
    }

    public boolean delete(long id) {
        certificateTagDao.deleteByCertificateId(id);
        return certificateDao.delete(id);
    }

    public Optional<CertificateDto> find(long id) {
        return certificateDao.find(id)
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class));
    }

    public List<CertificateDto> findCertificates(String tagName, String descriptionPart, String orderBy) {
        CertificateSearchSqlBuilder specification =
                new CertificateSearchSqlBuilder(tagName, descriptionPart, orderBy);
        if(orderBy != null && !specification.checkOrderBy()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid orderBy parameter");
        }
        String query = specification.getSqlQuery();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (tagName != null) {
            parameters.addValue("tag_name", tagName);
        }
        if (descriptionPart != null) {
            parameters.addValue("description", "%" + descriptionPart + "%");
        }
        return certificateDao.findCertificates(query, parameters)
                .stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class))
                .collect(Collectors.toList());
    }

    public void addCertificateTags(List<String> tags, long certificateId) {
        tags.forEach(tagName -> {
            Tag tag = tagDao.findByName(tagName).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                return tagDao.create(newTag);
            });
            certificateTagDao.create(certificateId, tag.getId());
        });
    }

    public void updateCertificateTags(List<String> tags, long certificateId) {
        List<Tag> certificateTagsBeforeUpdate = tagDao.findByCertificateId(certificateId);
        certificateTagsBeforeUpdate.forEach(tag -> {
            if (!tags.contains(tag.getName())) {
                certificateTagDao.delete(certificateId, tag.getId());
            }
        });
        List<String> certificateTagNamesBeforeUpdate = certificateTagsBeforeUpdate.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        List<String> tagsToAdd = tags.stream()
                .filter(tagName -> !certificateTagNamesBeforeUpdate.contains(tagName))
                .collect(Collectors.toList());
        addCertificateTags(tagsToAdd, certificateId);
    }

    public void addCertificateTag(Tag tag, long certificateId) {
        Tag tagToAdd = tagDao.findByName(tag.getName()).orElseGet(() ->
                tagDao.create(tag)
        );
        try {
            certificateTagDao.create(certificateId, tagToAdd.getId());
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "The certificate already has this tag");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no certificate with id = " + certificateId);
        }
    }

    public void deleteCertificateTag(long certificateId, long tagId) {
        if (certificateDao.find(certificateId).isPresent()) {
            Tag tagToDelete = tagDao.findByIdAndCertificateId(tagId, certificateId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Certificate with id " + certificateId +
                                        " doesnt have tag with id " + tagId)
                    );
            certificateTagDao.delete(certificateId, tagId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no certificate with id = " + certificateId);
        }
    }
}
