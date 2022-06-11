package com.gsnotes.services;

import java.util.List;


import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.utils.export.ExcelExporter;

public interface IInscriptionAnnuelleService {
	public void addInscriptionAnnuelle(InscriptionAnnuelle inscr);

	public void updateInscriptionAnnuelle(InscriptionAnnuelle inscr);

	public List<InscriptionAnnuelle> getAllInscriptionAnnuelles();

	public void deleteInscriptionAnnuelle(Long id);

	public InscriptionAnnuelle getInscriptionAnnuelleById(Long id);

	//public InscriptionAnnuelle getInscriptionAnnuelleByCin(String cin);

	public ExcelExporter prepareInscriptionAnnuelleExport(List<InscriptionAnnuelle> inscrs);
}
