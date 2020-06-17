package com.epam.esm.tag;

import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;


public class TagDaoTest {

    TagDao tagDao;

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
        tagDao = new TagDao(embeddedDatabase);
    }


    @Test
    public void createTagTest() {
        String expected = "tag for test";
        Tag tag = new Tag(expected);

        Tag result = tagDao.create(tag);

        assertTrue(result.getId() > 0);
        assertEquals(expected, result.getName());
    }

    @Test
    public void deleteTagTest() {
        int expected = 4;

        boolean result = tagDao.delete(1);

        assertEquals(expected, tagDao.findAll().size());
        assertTrue(result);
        assertFalse(tagDao.find(1).isPresent());
    }

    @Test
    public void findTagByIdTest() {
        Tag expected = new Tag(1, "tag one");

        Tag result = tagDao.find(1).orElseGet(null);

        assertEquals(expected, result);
    }

    @Test
    public void findAllTest() {
        int expectedSize = 5;

        List<Tag> result = tagDao.findAll();

        assertEquals(expectedSize, result.size());
    }

    @Test
    public void findByName() {
        Tag expected = new Tag(1, "tag one");

        Tag result = tagDao.findByName("tag one").get();

        assertEquals(expected, result);
    }

    @Test
    public void findByCertificateIdTest() {
        Tag tagOne = new Tag(3, "tag three");
        Tag tagTwo = new Tag(4, "fourth tag");
        List<Tag> expected = Arrays.asList(tagOne, tagTwo);

        List<Tag> result = tagDao.findByCertificateId(2);

        assertEquals(expected, result);
    }

    @Test
    public void findByNameAndCertificateIdTest() {
        Tag expected = new Tag(1, "tag one");

        Tag result = tagDao.findByNameAndCertificateId("tag one", 1).get();

        assertEquals(expected, result);
    }

    @After
    public void tearDown() {
       embeddedDatabase.shutdown();
    }
}
