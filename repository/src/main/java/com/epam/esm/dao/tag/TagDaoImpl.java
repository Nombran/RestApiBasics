package com.epam.esm.dao.tag;

import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao{

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND = "select * from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?)";
    private final String SQL_FIND_ALL = "select * from tag";
    private final String SQL_DELETE = "delete from tag where id = ?";

    @Autowired
    public TagDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean create(Tag tag) {
        return jdbcTemplate.update(SQL_INSERT, tag.getName()) > 0;
    }

    @Override
    public boolean update(Tag tag) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

    @Override
    public Optional<Tag> find(long id) {
        try {
            Tag tag = jdbcTemplate.queryForObject(SQL_FIND,
                    new Object[]{id},
                    new TagMapper());
            return Optional.ofNullable(tag);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new TagMapper());
    }
}
