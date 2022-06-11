package com.gsnotes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Etudiant;

public interface IEtudiantDao extends JpaRepository<Etudiant, Long>{
	public Etudiant getEtudiantByCin(String cin);
	public Optional<Etudiant> findById(Long id);

}
