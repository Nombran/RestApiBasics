package com.epam.esm.tag;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public boolean create(Tag tag) {
        return tagDao.create(tag);
    }

    @Override
    public boolean update(Tag tag) {
        return tagDao.update(tag);
    }

    @Override
    public boolean delete(long id) {
        return tagDao.delete(id);
    }

    @Override
    public Optional<Tag> find(long id) {
        return tagDao.find(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }
}
