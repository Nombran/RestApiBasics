package com.epam.esm.certificate;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.certificate.specification.CertificateSearchSqlBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

public class CertificateDaoTest {

    CertificateDao certificateDao;

    EmbeddedDatabase embeddedDatabase;

    @Before
    public void init() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .addScripts("insert-data.sql")
                .build();
        certificateDao = new CertificateDao(embeddedDatabase);
    }

    @Test
    public void createCertificateTest() {
        Certificate certificate = new Certificate("name", "description",
                new BigDecimal("12.6"), 12);

        Certificate expected = certificateDao.create(certificate);

        assertTrue(expected.getId() > 0);
        assertNotNull(expected.getCreationDate());
    }

    @Test
    public void updateCertificateTest() {
        Certificate certificate = certificateDao.find(1).get();
        certificate.setName("new name");
        certificate.setPrice(new BigDecimal("9.99"));

        boolean result = certificateDao.update(certificate);
        Certificate updated = certificateDao.find(1).get();

        assertTrue(result);
        assertEquals(updated.getName(), "new name");
        assertNotNull(updated.getModificationDate());
        assertEquals(updated.getPrice(), new BigDecimal("9.99"));
    }

    @Test
    public void deleteCertificateTest() {
        int expectedSize = certificateDao.findAll().size() - 1;

        boolean result = certificateDao.delete(4);
        int actualSize = certificateDao.findAll().size();

        assertTrue(result);
        assertEquals(expectedSize, actualSize);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void deleteCertificateExpectedExceptionTest() {
        certificateDao.delete(1);
    }

    @Test
    public void findCertificateById() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2020, 6, 9, 0, 0);
        Certificate expected = new Certificate(1, "certificate one", "description",
                new BigDecimal("12.5"), expectedDateTime, null, 5);

        Certificate result = certificateDao.find(1).get();

        assertEquals(expected, result);
    }

    @Test
    public void findAllCertificatesTest() {
        int expectedListSize = 4;

        List<Certificate> certificates = certificateDao.findAll();
        int actualListSize = certificates.size();

        assertEquals(expectedListSize, actualListSize);
    }

    @Test
    public void findCertificatesByParameters() {
        String tagName = "fifth tag";
        String query = new CertificateSearchSqlBuilder("fifth tag", null,null).getSqlQuery();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("tag_name", tagName);
        LocalDateTime expectedDateTime = LocalDateTime.of(2021,9,17,10,10);
        Certificate expectedCertificate = new Certificate(3, "certificate three", "third row",
                new BigDecimal("2.5"), expectedDateTime, null, 18);
        int expectedResultSize = 1;

        List<Certificate> result = certificateDao.findCertificates(query, mapSqlParameterSource);

        assertEquals(expectedResultSize, result.size());
        assertEquals(expectedCertificate.getName(), result.get(0).getName());
    }


}
