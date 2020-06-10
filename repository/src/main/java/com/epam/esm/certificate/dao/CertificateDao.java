package com.epam.esm.certificate.dao;

import com.epam.esm.certificate.mapper.CertificateMapper;
import com.epam.esm.certificate.model.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND = "select * from certificate where id = ?";
    private final String SQL_INSERT = "insert into certificate(name, description, price, creation_date," +
            " duration ) values(?,?,?,?,?)";
    private final String SQL_FIND_ALL = "select * from certificate";
    private final String SQL_UPDATE = "update certificate set name = ?, description = ?, price  = ?," +
            "modification_date = ?, duration = ? where id = ?";
    private final String SQL_DELETE = "delete from certificate where id = ?";



    @Autowired
    public CertificateDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean create(Certificate certificate) {
        return jdbcTemplate.update(SQL_INSERT, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), LocalDateTime.now(),
                certificate.getDuration()) > 0;
    }

    public boolean update(Certificate certificate) {
        return jdbcTemplate.update(SQL_UPDATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), LocalDateTime.now(),
                certificate.getDuration(), certificate.getId()) > 0;
    }

    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

    public Optional<Certificate> find(long id) {
        try {
            Certificate certificate = jdbcTemplate.queryForObject(SQL_FIND,
                    new Object[]{id},
                    new CertificateMapper());
            return Optional.ofNullable(certificate);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Certificate> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new CertificateMapper());
    }
}
