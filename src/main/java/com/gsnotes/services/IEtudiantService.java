package com.gsnotes.services;

import com.gsnotes.bo.Etudiant;


import java.util.List;
import java.util.Optional;

import com.gsnotes.utils.export.ExcelExporter;

public interface IEtudiantService {
	public void addEtudiant(Etudiant pEtudiant);

	public void updateEtudiant(Etudiant pEtudiant);

	public List<Etudiant> getAllEtudiants();

	public void deleteEtudiant(Long id);

	public Etudiant getEtudiantById(Long id);
	
	public Etudiant getEtudiantByCin(String cin);
	
	public ExcelExporter prepareEtudiantExport(List<Etudiant> etudiants);
	public Optional<Etudiant> findById(Long id);


}
