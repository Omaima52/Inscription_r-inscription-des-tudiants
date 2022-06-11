package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IInscriptionAnnuelleDao;
import com.gsnotes.services.IInscriptionAnnuelleService;
import com.gsnotes.utils.export.ExcelExporter;


@Service
@Transactional
public class InscriptionAnnuelleServiceImpl implements IInscriptionAnnuelleService{
	@Autowired
	private IInscriptionAnnuelleDao inscriptionDao;

	@Override
	public List<InscriptionAnnuelle> getAllInscriptionAnnuelles() {
		return inscriptionDao.findAll();
	}

	public void addInscriptionAnnuelle(InscriptionAnnuelle inscr) {
		
		inscriptionDao.save(inscr);

	}

	
	@Override
	public void updateInscriptionAnnuelle(InscriptionAnnuelle inscr) {
		inscriptionDao.save(inscr);
	}

	

	@Override
	public void deleteInscriptionAnnuelle(Long id) {
		inscriptionDao.deleteById(id);
		
	}

	@Override
	public InscriptionAnnuelle getInscriptionAnnuelleById(Long id) {
		return inscriptionDao.getById(id);
	}

//	@Override
//	public InscriptionAnnuelle getInscriptionAnnuelleByCin(String cin) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public ExcelExporter prepareInscriptionAnnuelleExport(List<InscriptionAnnuelle> inscrs) {
		String[] columnNames = new String[] { "ID ETUDIANT", "CNE", "NOM", "PRENOM", "ID NIVEAU ACTUEL", "Type" };
		String[][] data = new String[inscrs.size()][6];

		int i = 0;
		for (InscriptionAnnuelle ins : inscrs) {
			data[i][0] = ins.getEtudiant().getIdUtilisateur().toString();
			data[i][1] = ins.getEtudiant().getCne();
			data[i][2] = ins.getEtudiant().getNom();
			data[i][3] = ins.getEtudiant().getPrenom();
			data[i][4] = ins.getNiveau().getIdNiveau().toString();
			data[i][5] = ins.getType();
			i++;
		}

		return new ExcelExporter(columnNames, data, "inscriptions");
	}

//	@Override
//	public InscriptionAnnuelle getInscriptionAnnuelleByCin(String cin) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
