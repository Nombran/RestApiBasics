package com.epam.esm.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    public void create_CorrectTag_ShouldReturnTrue() {
        //Given
        Tag tag = new Tag("Tag for test");
        Mockito.when(tagDao.create(tag)).thenReturn(tag);

        //When
        boolean result = tagService.create(tag);

        //Then
        assertTrue(result);
        verify(tagDao, times(1)).create(tag);
    }

    @Test
    public void delete_CorrectCertificateId_ShouldReturnTrue() {
        //Given
        long tagId = 1;
        Mockito.when(tagDao.delete(1)).thenReturn(true);

        //When
        boolean result = tagService.delete(tagId);

        //Then
        verify(tagDao, times(1)).delete(tagId);
        assertTrue(result);
    }

    @Test
    public void find_ExistentTagId_ShouldReturnCorrectTag() {
        //Given
        long tagId = 1;
        Tag tag = new Tag(1, "Tag for test");
        Mockito.when(tagDao.find(1)).thenReturn(Optional.of(tag));

        //When
        Optional<Tag> result = tagService.find(tagId);

        //Then
        verify(tagDao, times(1)).find(tagId);
        assertEquals(tag, result.get());
    }

    @Test
    public void findAll_ShouldReturnAllTags() {
        //Given
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        Tag tagThree = new Tag(3, "tagThree");
        List<Tag> tagList = Arrays.asList(tagOne, tagTwo, tagThree);
        Mockito.when(tagDao.findAll()).thenReturn(tagList);

        //When
        List<Tag> result = tagService.findAll();

        //Then
        assertEquals(tagList, result);
        verify(tagDao, times(1)).findAll();
    }

    @Test
    public void findTagsByCertificateId_ExistentCertificateId_ShouldReturnNonEmptyList() {
        //Given
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        Tag tagThree = new Tag(3, "tagThree");
        List<Tag> tagList = Arrays.asList(tagOne, tagTwo, tagThree);
        Mockito.when(tagDao.findByCertificateId(anyLong())).thenReturn(tagList);
        Mockito.when(certificateDao.find(anyLong()))
                .thenReturn(Optional.of(new Certificate()));

        //When
        List<Tag> result = tagService.findTagsByCertificateId(anyLong());

        //Then
        verify(tagDao, times(1)).findByCertificateId(anyLong());
        assertEquals(tagList, result);
    }
}
