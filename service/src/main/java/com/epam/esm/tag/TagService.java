package com.epam.esm.tag;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TagService {
    private final TagDao tagDao;
    private final CertificateDao certificateDao;

    public TagService(TagDao tagDao,
                      CertificateDao certificateDao) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;

    }

    public boolean create(Tag tag) {
        try {
            return tagDao.create(tag) != null;
        } catch (DuplicateKeyException e) {
            log.error("Tag with name " + tag.getName() + "already exists");
            return false;
        }
    }

    public boolean delete(long id) {
        try {
            return tagDao.delete(id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete tag with id " + id +
                    " because of relationships with some certificate");
            return false;
        }
    }

    public Optional<Tag> find(long id) {
        return tagDao.find(id);
    }

    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    public List<Tag> findTagsByCertificateId(long id) {
        if(certificateDao.find(id).isPresent()) {
            return tagDao.findByCertificateId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no certificate with id = " + id);
        }
    }
}
