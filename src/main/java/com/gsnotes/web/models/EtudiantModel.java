package com.gsnotes.web.models;

public class EtudiantModel {
	private Long idEtudiant;

	private String nom;

	private String prenom;
	private String cne;

	private Long idNiveau;

	private String type;

	public EtudiantModel() {
		super();
	}

	@Override
	public String toString() {
		return "EtudiantModel [idEtudiant=" + idEtudiant + ", nom=" + nom + ", prenom=" + prenom + ", cne=" + cne
				+ ", idNiveau=" + idNiveau + ", type=" + type + "]";
	}

	public Long getIdEtudiant() {
		return idEtudiant;
	}

	public void setIdEtudiant(Long idEtudiant) {
		this.idEtudiant = idEtudiant;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public Long getIdNiveau() {
		return idNiveau;
	}

	public void setIdNiveau(Long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EtudiantModel(Long idEtudiant, String nom, String prenom, String cne, Long idNiveau, String type) {
		super();
		this.idEtudiant = idEtudiant;
		this.nom = nom;
		this.prenom = prenom;
		this.cne = cne;
		this.idNiveau = idNiveau;
		this.type = type;
	}

}
