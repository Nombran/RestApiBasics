package com.epam.esm.certificate.service;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.dto.CertificateDto;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.certificatetag.dao.CertificateTagDao;
import com.epam.esm.tag.TagService;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateService {

    private final CertificateDao certificateDao;

    private final TagDao tagDao;

    private final CertificateTagDao certificateTagDao;

    private final ModelMapper modelMapper;

    @Autowired
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

    public List<CertificateDto> findAll() {
        return certificateDao.findAll()
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
}
