package com.epam.esm.tag;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService {

    private final TagDao tagDao;
    private final CertificateDao certificateDao;

    @Autowired
    public TagService(TagDao tagDao,
                      CertificateDao certificateDao) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;

    }

    public boolean create(Tag tag) {
        try {
            tagDao.create(tag);
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public boolean delete(long id) {
        try {
            tagDao.delete(id);
            return true;
        } catch (DataIntegrityViolationException e) {
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
