package com.epam.esm.tag;

import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService {

    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
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
        return tagDao.findByCertificateId(id);
    }
}
