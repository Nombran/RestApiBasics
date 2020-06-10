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
public class TagDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND = "select * from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?)";
    private final String SQL_FIND_ALL = "select * from tag";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_FIND_BY_CERTIFICATE_ID = "select * from tag inner" +
            " join certificate_tag on tag.id = certificate_tag.tag_id where " +
            "certificate_tag.certificate_id = ?";

    @Autowired
    public TagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean create(Tag tag) {
        return jdbcTemplate.update(SQL_INSERT, tag.getName()) > 0;
    }

    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

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

    public List<Tag> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new TagMapper());
    }

    public List<Tag> findByCertificateId(long id) {
        return jdbcTemplate.query(SQL_FIND_BY_CERTIFICATE_ID, new Object[] {id}, new TagMapper());
    }
}
