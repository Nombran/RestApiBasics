package com.epam.esm.certificatetag.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CertificateTagDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_INSERT = "insert into certificate_tag(certificate_id, tag_id)" +
            " values(?, ?)";

    private final String SQL_DELETE = "delete from certificate_tag where certificate_id = ? " +
            "and tag_id = ?";

    private final String SQL_DELETE_BY_CERTIFICATE_ID = "delete from certificate_tag where " +
            "certificate_id = ?";

    @Autowired
    public CertificateTagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean create(long certificateId, long tagId) {
        return jdbcTemplate.update(SQL_INSERT, certificateId, tagId) > 0;
    }

    public boolean delete(long certificateId, long tagId) {
        return jdbcTemplate.update(SQL_DELETE, certificateId, tagId) > 0;
    }

    public boolean deleteByCertificateId(long certificateId) {
        return jdbcTemplate.update(SQL_DELETE_BY_CERTIFICATE_ID, certificateId) > 0;
    }

}