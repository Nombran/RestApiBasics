package com.epam.esm.tag;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public boolean create(Tag tag) {
        return tagDao.create(tag);
    }

    public boolean delete(long id) {
        return tagDao.delete(id);
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
