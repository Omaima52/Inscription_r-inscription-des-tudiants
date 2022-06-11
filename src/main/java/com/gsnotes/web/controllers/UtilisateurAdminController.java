package com.gsnotes.web.controllers;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gsnotes.bo.CadreAdministrateur;
import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.services.IInscriptionAnnuelleService;
import com.gsnotes.services.IPersonService;
import com.gsnotes.utils.export.ExcelExporter;
import com.gsnotes.web.models.InscriptionModel;
import com.gsnotes.web.models.PersonModel;
import com.gsnotes.web.models.UserAndAccountInfos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Ce controleur gère les personnes de type Etudiant, Enseignant et Cadre Admin
 * 
 * @author Boudaa
 *
 */

@Controller
@RequestMapping("/admin")
public class UtilisateurAdminController {

	@Autowired
	private IPersonService personService;

	@Autowired
	private IInscriptionAnnuelleService inscriptipnService;

	@Autowired
	private IEtudiantService etudientService;

	@Autowired
	private HttpSession httpSession;

	/** Utilisé pour la journalisation */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public UtilisateurAdminController() {

	}

	@RequestMapping(value = "showForm", method = RequestMethod.GET)
	public String showForm(@RequestParam int typePerson, Model model) {

		PersonModel pmodel = new PersonModel(typePerson);
		model.addAttribute("personModel", pmodel);

		// Nous avons choisit d'utiliser une classe modèle personnalisée
		// définie par PersonModel pour une meilleur flexibilité

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();
		// On copie les données des personnes vers le modèle
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				// permet de copier les données d'un objet à l'autre à codition
				// d'avoir les meme attributs (getters/setters)
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				// On fixe le type (cet attribut ne sera pas copié automatiquement)
				pm.setTypePerson(PersonModel.TYPE_STUDENT);

				// Mettre la personne dans le modèle
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {

				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		// Mettre la liste des personnes dans le modèle de Spring MVC
		model.addAttribute("personList", modelPersons);

		return "admin/form";
	}

	@RequestMapping(value = "addPerson", method = RequestMethod.POST)
	public String process(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model, HttpServletRequest rq) {

		// En cas d'erreur de validation
		if (bindingResult.hasErrors()) {
			// rq.setAttribute("typePerson", +person.getTypePerson());
			return "admin/form";
		}

		// Copier les données de l'objet PersonModel vers l'objet Etudiant (cas du
		// formulaire de l'étudiant)
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.addPerson(etd);

		}
		// Copier les données de l'objet PersonModel vers l'objet Enseignant (cas du
		// formulaire de l'Enseignant)

		else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.addPerson(prof);

		}
		// Copier les données de l'objet PersonModel vers l'objet CadreAdministrateur
		// (cas du
		// formulaire de CadreAdministrateur)
		else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.addPerson(ca);

		}

		// rediriger vers l'action shwoForm avec le meme type de personne
		return "redirect:/admin/showForm?typePerson=" + person.getTypePerson();
	}

	@RequestMapping(value = "updatePersonForm/{idPerson}", method = RequestMethod.GET)
	public String updatePersonForm(@PathVariable int idPerson, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonById(Long.valueOf(idPerson));

		// On construit le modèle
		PersonModel pm = new PersonModel();

		// En fonction due type de l'utilisateur à modifier
		// Ceci va nous pemettre d'afficher un formulaire adapté
		// slon le type de la personne
		if (utl instanceof Etudiant) {
			BeanUtils.copyProperties((Etudiant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_STUDENT);
		} else if (utl instanceof Enseignant) {
			BeanUtils.copyProperties((Enseignant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_PROF);
		} else if (utl instanceof CadreAdministrateur) {
			BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
		}

		// Initialiser le modele avec la personne
		model.addAttribute("personModel", pm);

		return "admin/updateForm";
	}

	@RequestMapping(value = "serachPerson", method = RequestMethod.GET)
	public String serachPerson(@RequestParam String cin, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonByCin(cin);

		if (utl == null) {

			// Initialiser le modele avec la personne
			model.addAttribute("personModel", new ArrayList<PersonModel>());
		} else {

			// On construit le modèle
			PersonModel pm = new PersonModel();

			// En fonction due type de l'utilisateur à modifier
			// Ceci va nous pemettre d'afficher un formulaire adapté
			// slon le type de la personne
			if (utl instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
			} else if (utl instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
			} else if (utl instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);

			}
			List<PersonModel> modelPersons = new ArrayList<PersonModel>();
			modelPersons.add(pm);
			// Initialiser le modele avec la personne
			model.addAttribute("personList", modelPersons);
		}
		return "admin/listPersons";
	}

	@RequestMapping("updatePerson")
	public String updatePerson(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model) {

		// En cas d'erreur
		if (bindingResult.hasErrors()) {

			return "admin/updateForm";
		}

		// On copie les données du modèle vers l'objet métier puis on appel le service
		// pour faire la mise à jour
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.updatePerson(etd);

		} else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.updatePerson(prof);

		} else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.updatePerson(ca);

		}

		// Mettre le message de succès dans le modèle
		model.addAttribute("msg", "Opération effectuée avec succès");

		return "admin/updateForm";
	}

	@RequestMapping("managePersons")
	public String managePersons(Model model) {

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();

		// Copier les objets metier vers PersonModel plus flexible
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		model.addAttribute("personList", modelPersons);

		return "admin/listPersons";
	}

	@RequestMapping(value = "deletePerson/{idPerson}", method = RequestMethod.GET)
	public String delete(@PathVariable int idPerson) {

		personService.deletePerson(Long.valueOf(idPerson));

		return "redirect:/admin/managePersons";
	}

	@GetMapping("exportPersons")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Utilisateur> persons = personService.getAllPersons();

		ExcelExporter excelExporter = personService.preparePersonExport(persons);

		excelExporter.export(response);
	}
	
	
	
	@RequestMapping("manageStudents")
	public String manageStudents(Model model) {

		List<InscriptionAnnuelle> inscriptions = inscriptipnService.getAllInscriptionAnnuelles();
		List<InscriptionModel> modelPersons = new ArrayList<InscriptionModel>();

		// Copier les objets metier vers PersonModel plus flexible
		for (int i = 0; i < inscriptions.size(); i++) {
			InscriptionModel modelin = new InscriptionModel();
			modelin.setIdEtudiant(inscriptions.get(i).getEtudiant().getIdUtilisateur());
			modelin.setCne(inscriptions.get(i).getEtudiant().getCne());
			
				
			modelPersons.add(modelin);
			
		}

		model.addAttribute("studentList", modelPersons);

		return "admin/listStudents";
	}

	@RequestMapping("upload")
	public String upload() {

		return "admin/uploadExcel";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("import")
	public String importExcelData(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			Model model) throws IOException {
		
		//Vérifier que le format du fichier est correct (nombre de colonnes, types des 
		//données,…) 

//		String fileName = file.getOriginalFilename();
//		if (!fileName.endsWith(".xlsx") || !fileName.endsWith(".xls")) {
//			return "ErrorFileformat";
//		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		System.out.println("-------Sheet has '" + noOfColumns + "' columns------");
		if (noOfColumns != 6) {

			System.out.println("column number should be 6!!");

			return "errorColumnFile";
		}

		List<InscriptionAnnuelle> ins = new ArrayList<InscriptionAnnuelle>();
		List<Etudiant> studentsList = new ArrayList<Etudiant>();
		

		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			Etudiant etudiant = new Etudiant();
			Niveau niveau = new Niveau();

			InscriptionAnnuelle in = new InscriptionAnnuelle();

			XSSFRow row = sheet.getRow(i);

			String cne = row.getCell(1).getStringCellValue().toString();
			String nom = row.getCell(2).getStringCellValue().toString();
			String prenom = row.getCell(3).getStringCellValue().toString();
			String type = row.getCell(5).getStringCellValue();
			long id = (long) row.getCell(0).getNumericCellValue();
			long idNiveau = (long) row.getCell(4).getNumericCellValue();

			

			

			

			if (row.getCell(1).getCellType() == CellType.STRING && row.getCell(2).getCellType() == CellType.STRING
					&& row.getCell(3).getCellType() == CellType.STRING
					&& row.getCell(5).getCellType() == CellType.STRING) {
				etudiant.setCne(cne);
				etudiant.setNom(nom);
				etudiant.setPrenom(prenom);
				in.setType(type);

			}
			if (row.getCell(0).getCellType() == CellType.NUMERIC && row.getCell(4).getCellType() == CellType.NUMERIC) {
				etudiant.setIdUtilisateur(id);
				niveau.setIdNiveau(idNiveau);
			}

			in.setNiveau(niveau);
			in.setEtudiant(etudiant);

			ins.add(in);
			etudiant.setInscriptions(ins);
			studentsList.add(etudiant);

			System.out.println(etudiant.toString());
   

		}
		
		
		
		
		
		
		
	
		List<InscriptionModel> modelInscr = new ArrayList<InscriptionModel>();
	
		//Les identifiant des niveaux existent bien dans la base de données de gestion 
		//des notes
		for (int i = 0; i < ins.size(); i++) {
			if (ins.get(i).getNiveau().getIdNiveau() > 29) {
				System.out.println("Hello false Niveau id");
				InscriptionModel modelIns = new InscriptionModel();
				modelIns.setIdEtudiant(ins.get(i).getEtudiant().getIdUtilisateur());
				modelIns.setIdNiveau(ins.get(i).getNiveau().getIdNiveau());
				modelIns.setCne(ins.get(i).getEtudiant().getCne());
				modelIns.setNom(ins.get(i).getEtudiant().getNom());
				modelIns.setPrenom(ins.get(i).getEtudiant().getPrenom());
				modelIns.setType(ins.get(i).getType());
				modelInscr.add(modelIns);
				model.addAttribute("inscriptionsList", modelInscr);
				return "errorNiveau";	
			}
			
			
		}
		
		
		for (int i = 0; i < ins.size(); i++) {
			long iddd = ins.get(i).getEtudiant().getIdUtilisateur();
			
			Optional<Etudiant> e = etudientService.findById(iddd);
			
			//Pour chaque ancien étudiant, elle ajoute une nouvelle inscription pour le 
			//niveau indiqué dans le fichier
			if (e.isPresent()) {
				Etudiant et = e.get();
				
				
				//Si la condition C est vérifiée, mais les données CNE, Nom et Prénom présentes 
				//dans le fichier sont différentes des données présentes en base de données,
				if (!et.getCne().equals(ins.get(i).getEtudiant().getCne()) &&  
					!et.getNom().equals(ins.get(i).getEtudiant().getNom()) &&
					!et.getPrenom().equals(ins.get(i).getEtudiant().getPrenom())
					){
					return "modifyInfo";
				}
				
				List<InscriptionAnnuelle> inscriEt = new ArrayList<InscriptionAnnuelle>();
				inscriEt.add(ins.get(i));
				et.setInscriptions(inscriEt);
			}else {
				try {
					//Pour chaque nouvel étudiant elle l’ajoute d’abord en base de données et elle 
					//lui ajoute par la suite une inscription dans le niveau indiqué dans le fichier.
					if (!e.isPresent()  ) {
						System.out.println("e = null------------------------------");
						Etudiant et = ins.get(i).getEtudiant();
						System.out.println(et.getIdUtilisateur());
						List<InscriptionAnnuelle> inscriEt = new ArrayList<InscriptionAnnuelle>();
						inscriEt.add(ins.get(i));
						
						
						System.out.println(et.getIdUtilisateur());
						et.setCne(ins.get(i).getEtudiant().getCne());
						et.setNom(ins.get(i).getEtudiant().getNom());
						et.setPrenom(ins.get(i).getEtudiant().getPrenom());
						et.setInscriptions(inscriEt);
						etudientService.addEtudiant(et);
					}
					
					
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
				
				 
			}
				
			
		}
		
		

		 return "admin/uploadExcel";
	}

}