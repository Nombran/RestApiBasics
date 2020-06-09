package com.epam.esm.certificate;

import com.epam.esm.dao.certificate.CertificateDao;
import com.epam.esm.model.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService{

    private final CertificateDao certificateDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Override
    public boolean create(Certificate certificate) {
        return certificateDao.create(certificate);
    }

    @Override
    public boolean update(Certificate certificate) {
        return certificateDao.update(certificate);
    }

    @Override
    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    @Override
    public Optional<Certificate> find(long id) {
        return certificateDao.find(id);
    }

    @Override
    public List<Certificate> findAll() {
        return certificateDao.findAll();
    }
}
