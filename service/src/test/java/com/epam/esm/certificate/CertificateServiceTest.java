package com.epam.esm.certificate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.dto.CertificateDto;
import com.epam.esm.certificate.model.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.certificatetag.dao.CertificateTagDao;
import com.epam.esm.tag.dao.TagDao;
import com.epam.esm.tag.model.Tag;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

public class CertificateServiceTest {

    @InjectMocks
    CertificateService certificateService;

    @Mock
    CertificateDao certificateDao;

    @Mock
    TagDao tagDao;

    @Mock
    CertificateTagDao certificateTagDao;

    @Spy
    ModelMapper modelMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void createCertificateTest() {
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        List<Tag> tagsInDB = Arrays.asList(tagOne, tagTwo);
        Mockito.when(certificateTagDao.create(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(tagDao.findByName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return tagsInDB.stream()
                    .filter(tag -> tag.getName().equals(tagName))
                    .findFirst();
        });
        CertificateDto certificateDto = new CertificateDto("name", "description", new BigDecimal("12.6"),
                5, Arrays.asList("tagOne", "tagTwo"));
        Mockito.when(certificateDao.create(any(Certificate.class))).thenAnswer(invocation -> {
            Certificate certificate = invocation.getArgument(0);
            certificate.setId(anyLong());
            return certificate;
        });
        certificateService.create(certificateDto);

        verify(certificateDao, times(1)).create(any(Certificate.class));
        verify(tagDao, times(2)).findByName(anyString());
        verify(certificateTagDao, times(2)).create(anyLong(), anyLong());
    }

    @Test
    public void updateCertificateTest() {
        Mockito.when(certificateDao.update(any(Certificate.class))).thenReturn(true);
        CertificateDto certificateDto = new CertificateDto(1, "name", "description",
                new BigDecimal("12.6"), LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                5, Arrays.asList("tagOne", "tagThree"));
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        List<Tag> pastCertificateTags = Arrays.asList(tagOne, tagTwo);
        List<Tag> tagsInDB = Arrays.asList(tagOne, tagTwo);
        Mockito.when(tagDao.create(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(3);
            return tag;
        });
        Mockito.when(tagDao.findByCertificateId(1)).thenReturn(pastCertificateTags);
        Mockito.when(certificateTagDao.create(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(certificateTagDao.delete(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(tagDao.findByName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return tagsInDB.stream()
                    .filter(tag -> tag.getName().equals(tagName))
                    .findFirst();
        });

        certificateService.update(certificateDto);

        verify(certificateDao, times(1)).update(certificate);
        verify(tagDao, times(1)).findByCertificateId(1);
        verify(certificateTagDao, times(1)).delete(1,2);
        verify(tagDao,times(1)).findByName("tagThree");
        verify(tagDao, times(1)).create(any(Tag.class));
        verify(certificateTagDao, times(1)).create(1,3);
    }

    @Test
    public void deleteCertificateTest() {
        Mockito.when(certificateTagDao.delete(anyLong(),anyLong())).thenReturn(true);
        Mockito.when(certificateDao.delete(1)).thenReturn(true);

        certificateService.delete(1);

        verify(certificateTagDao,times(1)).deleteByCertificateId(anyLong());
        verify(certificateDao, times(1)).delete(1);
    }

    @Test
    public void findCertificateTest() {
        Certificate certificate = new Certificate(1, "name", "description",
                new BigDecimal("12.6"), LocalDateTime.now(), null, 12);
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        List<Tag> certificateTags = Arrays.asList(tagOne, tagTwo);
        Mockito.when(certificateDao.find(1)).thenReturn(Optional.of(certificate));
        Mockito.when(tagDao.findByCertificateId(1)).thenReturn(certificateTags);
        List<String> tagsInDto = certificateTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        CertificateDto expected = new CertificateDto(1, "name", "description",
                new BigDecimal("12.6"), LocalDateTime.now(), null, 12, tagsInDto);
        Mockito.when(modelMapper.map(certificate, CertificateDto.class)).thenReturn(expected);

        CertificateDto result = certificateService.find(1).get();

        assertEquals(expected, result);
        verify(certificateDao, times(1)).find(1);
    }

    @Test()
    public void findCertificatesTest() {
        Certificate certificate = new Certificate(1, "name", "description",
                new BigDecimal("12.6"), LocalDateTime.now(), null, 12);
        Mockito.when(certificateDao.findCertificates(anyString(), any(MapSqlParameterSource.class)))
                .thenReturn(Collections.singletonList(certificate));

        certificateService.findCertificates("tagName", "descriptionPart",null);

        verify(certificateDao,times(1)).findCertificates(anyString(),
                any(MapSqlParameterSource.class));
    }

    @Test(expected = ResponseStatusException.class)
    public void findCertificateWithInvalidOrderByPartTest() {
        certificateService.findCertificates("tagName",null, "invalid");
    }

    @Test
    public void addCertificateTagsTest() {
        List<Tag> tagsInDB = Collections.emptyList();
        List<String> tagsForSave = Arrays.asList("tagOne", "tagTwo");
        Mockito.when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        Mockito.when(tagDao.create(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(4);
            return tag;
        });
        Mockito.when(certificateTagDao.create(anyLong(), anyLong())).thenReturn(true);

        certificateService.addCertificateTags(tagsForSave, anyLong());

        verify(tagDao,times(2)).create(any(Tag.class));
        verify(tagDao,times(2)).findByName(anyString());
        verify(certificateTagDao,times(2)).create(anyLong(),anyLong());
    }

    @Test
    public void updateCertificatesTest() {
        Tag tagOne = new Tag(1, "tagOne");
        Tag tagTwo = new Tag(2, "tagTwo");
        List<Tag> tagsInDB = Arrays.asList(tagOne, tagTwo);
        List<String> tagsForUpdate = Arrays.asList("tagOne", "tagThree");
        Mockito.when(tagDao.create(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(3);
            return tag;
        });
        Mockito.when(tagDao.findByCertificateId(1)).thenReturn(tagsInDB);
        Mockito.when(certificateTagDao.create(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(certificateTagDao.delete(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(tagDao.findByName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return tagsInDB.stream()
                    .filter(tag -> tag.getName().equals(tagName))
                    .findFirst();
        });

        certificateService.updateCertificateTags(tagsForUpdate, 1);

        verify(tagDao, times(1)).findByCertificateId(1);
        verify(certificateTagDao, times(1)).delete(1,2);
        verify(tagDao,times(1)).findByName("tagThree");
        verify(tagDao, times(1)).create(any(Tag.class));
        verify(certificateTagDao, times(1)).create(1,3);
    }

    @Test
    public void addCertificateTagTest() {
        Tag tagOne = new Tag(1, "tagOne");
        Mockito.when(tagDao.findByName("tagOne")).thenReturn(Optional.of(tagOne));
        Mockito.when(certificateTagDao.create(anyLong(), anyLong())).thenReturn(true);

        certificateService.addCertificateTag(tagOne,1);

        verify(tagDao, times(1)).findByName("tagOne");
        verify(certificateTagDao, times(1)).create(anyLong(), anyLong());
    }


}
