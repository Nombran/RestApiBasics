package com.epam.esm.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    CertificateDao certificateDao;

    @Mock
    TagDao tagDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTagTest() {
        Tag tag = new Tag("Tag for test");
        Mockito.when(tagDao.create(tag)).thenReturn(tag);

        boolean result = tagService.create(tag);

        verify(tagDao, times(1)).create(tag);
        assertTrue(result);
    }

    @Test
    public void deleteTagTest() {
        long tagId = 1;
        Mockito.when(tagDao.delete(1)).thenReturn(true);

        boolean result = tagService.delete(tagId);

        verify(tagDao, times(1)).delete(tagId);
        assertTrue(result);
    }

    @Test
    public void findTagTest() {
        long tagId = 1;
        Tag tag = new Tag(1, "Tag for test");
        Mockito.when(tagDao.find(1)).thenReturn(Optional.of(tag));

        Optional<Tag> result = tagService.find(tagId);

        verify(tagDao, times(1)).find(tagId);
        assertEquals(tag, result.get());
    }

    @Test
    public void findAllTagsTest() {
        Tag tagOne = new Tag(1,"tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        Tag tagThree = new Tag(3, "tagThree");
        List<Tag> tagList = Arrays.asList(tagOne,tagTwo,tagThree);
        Mockito.when(tagDao.findAll()).thenReturn(tagList);

        List<Tag> result = tagService.findAll();

        verify(tagDao, times(1)).findAll();
        assertEquals(tagList, result);
    }

    @Test
    public void findTagsByCertificateId() {
        Tag tagOne = new Tag(1,"tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        Tag tagThree = new Tag(3, "tagThree");
        List<Tag> tagList = Arrays.asList(tagOne,tagTwo,tagThree);
        Mockito.when(tagDao.findByCertificateId(anyLong())).thenReturn(tagList);
        Mockito.when(certificateDao.find(anyLong()))
                .thenReturn(Optional.of(new Certificate()));

        List<Tag> result = tagService.findTagsByCertificateId(anyLong());

        verify(tagDao, times(1)).findByCertificateId(anyLong());
        assertEquals(tagList, result);
    }
}
