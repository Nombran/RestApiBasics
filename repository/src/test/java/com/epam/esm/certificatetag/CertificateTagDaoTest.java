package com.epam.esm.certificatetag;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificatetag.dao.CertificateTagDao;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

public class CertificateTagDaoTest {

    TagDao tagDao;

    CertificateTagDao certificateTagDao;

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
        certificateTagDao = new CertificateTagDao(embeddedDatabase);
        tagDao = new TagDao(embeddedDatabase);
    }

    @Test
    public void createCertificateTagTest() {
        Tag tag = tagDao.find(1).get();
        int sizeBeforeCreate = tagDao.findByCertificateId(3).size();

        boolean result = certificateTagDao.create(3, 1);
        int actualSize = tagDao.findByCertificateId(3).size();
        List<Tag> tagsByCertificateId = tagDao.findByCertificateId(3);

        assertTrue(result);
        assertEquals(sizeBeforeCreate + 1, actualSize);
        assertEquals(tag, tagsByCertificateId.get(0));
    }

    @Test
    public void deleteCertificateTag() {
        certificateTagDao.delete(1,1);

        Optional<Tag> result = tagDao.findByNameAndCertificateId("tag one", 1);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteByCertificateId() {
        certificateTagDao.deleteByCertificateId(1);

        boolean result = tagDao.findByCertificateId(1).size() == 0;

        assertTrue(result);
    }
}
