package com.gsnotes.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.dao.IEtudiantDao;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class EtudiantServiceImpl implements IEtudiantService{
	@Autowired
	private IEtudiantDao etudiantDao;

	
	@Override
	public List<Etudiant> getAllEtudiants() {

		return etudiantDao.findAll();
	} 
	
	@Override
	public void addEtudiant(Etudiant pEtudiant) {
		etudiantDao.save(pEtudiant);
		
	}
	
	@Override
	public void deleteEtudiant(Long id) {
		etudiantDao.deleteById(id);

	}
	@Override
	public Etudiant getEtudiantById(Long id) {
		return etudiantDao.getById(id);

	}

	@Override
	public void updateEtudiant(Etudiant pPerson) {
		etudiantDao.save(pPerson);

	}
	@Override
	public ExcelExporter prepareEtudiantExport(List<Etudiant> etudiants) {
		String[] columnNames = new String[] { "ID ETUDIANT", "CNE", "NOM", "PRENOM", "ID NIVEAU ACTUEL", "Type" };
		String[][] data = new String[etudiants.size()][6];

		int i = 0;
		for (Etudiant e : etudiants) {
			data[i][0] = e.getIdUtilisateur().toString();
			data[i][1] = e.getCne();
			data[i][2] = e.getNom();
			data[i][3] = e.getPrenom();
			data[i][4] = e.getInscriptions().toString();
			i++;
		}

		return new ExcelExporter(columnNames, data, "persons");

	}
	@Override
	public Etudiant getEtudiantByCin(String cin) {

		return etudiantDao.getEtudiantByCin( cin);

	}
	public Optional<Etudiant> findById(Long id){
		return etudiantDao.findById(id);
	}

	

	

}
