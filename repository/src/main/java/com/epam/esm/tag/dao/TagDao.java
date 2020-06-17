package com.epam.esm.tag.dao;

import com.epam.esm.tag.mapper.TagMapper;
import com.epam.esm.tag.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao {

    private final JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_FIND = "select * from tag where id = ?";
    private final String SQL_INSERT = "insert into tag (name) values (:name)";
    private final String SQL_FIND_ALL = "select * from tag";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_FIND_BY_CERTIFICATE_ID = "select * from tag inner" +
            " join certificate_tag on tag.id = certificate_tag.tag_id where " +
            "certificate_tag.certificate_id = ?";
    private final String SQL_FIND_BY_NAME = "select * from tag where name = ?";
    private final String SQL_FIND_BY_ID_AND_CERTIFICATE_ID = "select * from tag " +
            "inner join certificate_tag on tag.id = certificate_tag.tag_id " +
            "where tag.id = ? and certificate_tag.certificate_id = ?";


    @Autowired
    public TagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Tag create(Tag tag) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", tag.getName());
        namedParameterJdbcTemplate.update(SQL_INSERT, parameters, holder, new String[] { "id" });
        tag.setId(holder.getKey().longValue());
        return tag;
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

    public Optional<Tag> findByName(String name) {
        try {
        Tag tag = jdbcTemplate.queryForObject(SQL_FIND_BY_NAME,
                new Object[]{name},
                new TagMapper());
        return Optional.ofNullable(tag);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Tag> findByIdAndCertificateId(long id, long certificateId) {
        try {
            Tag tag = jdbcTemplate.queryForObject(SQL_FIND_BY_ID_AND_CERTIFICATE_ID,
                    new Object[]{id, certificateId},
                    new TagMapper());
            return Optional.ofNullable(tag);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
