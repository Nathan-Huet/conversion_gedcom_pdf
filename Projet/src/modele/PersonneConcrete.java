package modele;

import java.util.LinkedList;

import modele.infos.Evenement;
import modele.infos.Fichier;
import modele.infos.Genre;
import modele.infos.Information;
import modele.infos.Lien;
import modele.infos.Nom;
import modele.infos.Note;
import modele.infos.Prenom;
import modele.infos.Profession;
import modele.infos.Surnom;

public class PersonneConcrete extends Personne {

	public PersonneConcrete(String numPersonne) {
		super(numPersonne);
	}

	/**
	 * methode permettant d'obtenir la liste des noms de famille de la
	 * PersonneConcrete
	 * 
	 * @return liste des noms de famille de la PersonneConcrete
	 */
	public LinkedList<String> getNoms() {
		return getInformation(Nom.KEY);
	}

	/**
	 * methode permettant d'obtenir la liste des prenoms de la PersonneConcrete
	 * 
	 * @return liste des prenoms de la PersonneConcrete
	 */
	public LinkedList<String> getPrenoms() {
		return getInformation(Prenom.KEY);
	}

	/**
	 * methode permettant d'obtenir le genre d'une PersonneConcrete
	 * 
	 * @return le genre de la PersonneConcrete
	 */
	public LinkedList<String> getGenre() {
		return getInformation(Genre.KEY);
	}

	/**
	 * methode permettant d'obtenir la liste des surnoms de la PersonneConcrete
	 * 
	 * @return liste des surnoms de la PersonneConcrete
	 */
	public LinkedList<String> getSurnoms() {
		return getInformation(Surnom.KEY);
	}

	/**
	 * methode permettante d'obtenir la liste des notes de la PersonneConcrete
	 * 
	 * @return liste des notes de la PersonneConcrete
	 */
	public LinkedList<String> getNotes() {
		return getInformation(Note.KEY);
	}

	/**
	 * methode permettant d'obtenir la liste des professions de la PersonneConcrete
	 * 
	 * @return liste des professions de la PersonneConcrete
	 */
	public LinkedList<String> getProfessions() {
		return getInformation(Profession.KEY);
	}

	/**
	 * methode permettant d'obtenir la liste des liens concernant la
	 * PersonneConcrete
	 * 
	 * @return liste des liens concernant la PersonneConcrete
	 */
	public LinkedList<String> getLiens() {
		return getInformation(Lien.KEY);
	}

	/**
	 * methode permettant d'ajouter un nom a la liste des noms de la
	 * PersonneConcrete
	 * 
	 * @param nom nom a ajouter
	 */
	public void ajouterNom(String nom) {
		if (!listeDesInfos.containsKey(Nom.KEY)) {
			listeDesInfos.put(Nom.KEY, new Nom());
		}
		ajouterInformation(Nom.KEY, nom);
	}

	/**
	 * methode permettant d'ajouter un prenom a la liste des prenoms de la
	 * PersonneConcrete
	 * 
	 * @param prenom prenom a ajouter
	 */
	public void ajouterPrenom(String prenom) {
		if (!listeDesInfos.containsKey(Prenom.KEY)) {
			listeDesInfos.put(Prenom.KEY, new Prenom());
		}
		ajouterInformation(Prenom.KEY, prenom);
	}

	/**
	 * methode permettant d'ajouter un surnom a la liste des surnoms de la
	 * PersonneConcrete
	 * 
	 * @param surnom surnom a ajouter
	 */
	public void ajouterSurnom(String surnom) {
		if (!listeDesInfos.containsKey(Surnom.KEY)) {
			listeDesInfos.put(Surnom.KEY, new Surnom());
		}
		ajouterInformation(Surnom.KEY, surnom);
	}

	/**
	 * methode permettant d'ajouter une note a la liste des notes de la
	 * PersonneConcrete
	 * 
	 * @param note note a ajouter
	 */
	public void ajouterNote(String note) {
		if (!listeDesInfos.containsKey(Note.KEY)) {
			listeDesInfos.put(Note.KEY, new Note());
		}
		ajouterInformation(Note.KEY, note);
	}

	/**
	 * methode permettant d'ajouter une profession a la liste des professions de la
	 * PersonneConcrete
	 * 
	 * @param profession profession a ajouter
	 */
	public void ajouterProfession(String profession) {
		if (!listeDesInfos.containsKey(Profession.KEY)) {
			listeDesInfos.put(Profession.KEY, new Profession());
		}
		ajouterInformation(Profession.KEY, profession);
	}

	/**
	 * methode permettant d'ajouter un lien a la liste des liens de la
	 * PersonneConcrete
	 * 
	 * @param lien lien a ajouter
	 */
	public void ajouterLiens(String lien) {
		if (!listeDesInfos.containsKey(Lien.KEY)) {
			listeDesInfos.put(Lien.KEY, new Lien());
		}
		ajouterInformation(Lien.KEY, lien);
	}

	/**
	 * methode permettant de modifier le genre d'une PersonneConcrete
	 * 
	 * @param sexe genre de la PersonneConcrete
	 */
	public void ajouterGenre(String genre) {
		if (!listeDesInfos.containsKey(Genre.KEY)) {
			listeDesInfos.put(Genre.KEY, new Genre());
		}
		ajouterInformation(Genre.KEY, genre);
	}

	/**
	 * retourne le premier nom de la liste des noms de la personne s'il y en a au
	 * moins un suivi du premier prenom de la liste des prenoms de la personne s'il
	 * y en a au moins un
	 */
	@Override
	public String toString() {
		String nom = "";
		String prenom = "";

		if (getNoms().size() >= 1) {
			nom = getNoms().getFirst();
		} else {
			nom = "Nom inconnu";
		}
		if (getPrenoms().size() >= 1) {
			prenom = getPrenoms().getFirst();
		} else {
			prenom = "Prenom inconnu";
		}

		return prenom + " " + nom;
	}

	@Override
	public void ajouterInformation(String key, String info) {
		if (!listeDesInfos.containsKey(key)) {
			Information classInfo;
			classInfo = selectionnerClasseInfo(key);
			listeDesInfos.put(key, classInfo);
		}
		listeDesInfos.get(key).ajouterInformation(info);
	}

	public Information selectionnerClasseInfo(String key) {
		switch (key) {
		case Nom.KEY:
			return new Nom();
		case Prenom.KEY:
			return new Prenom();
		case Surnom.KEY:
			return new Surnom();
		case Genre.KEY:
			return new Genre();
		case Note.KEY:
			return new Note();
		case Evenement.KEY:
			return new Evenement();
		case Fichier.KEY:
			return new Fichier();
		case Lien.KEY:
			return new Lien();
		case Profession.KEY:
			return new Profession();
		default:
			break;
		}
		return null;
	}

	@Override
	public int compareTo(Personne personne) {
		int personneNbNom, personneNbPrenom, thisNbNom, thisNbPrenom, compare;
		personneNbNom = personne.getInformation(Nom.KEY).size();
		personneNbPrenom = personne.getInformation(Prenom.KEY).size();
		thisNbNom = this.getInformation(Nom.KEY).size();
		thisNbPrenom = this.getInformation(Prenom.KEY).size();

		if (thisNbNom == 0 && personneNbNom == 0) {
			if (thisNbPrenom == 0 && personneNbPrenom == 0)
				compare = 0;
			else
				compare = this.getInformation(Prenom.KEY).getFirst()
						.compareTo(personne.getInformation(Prenom.KEY).getFirst());

		} else if (thisNbNom == 0 && thisNbNom != 0)
			compare = -1;
		else if (thisNbNom != 0 && thisNbNom == 0)
			compare = 1;
		else {
			compare = this.getInformation(Nom.KEY).getFirst().compareTo(personne.getInformation(Nom.KEY).getFirst());
			if (compare == 0) {
				if (thisNbPrenom == 0 && personneNbPrenom == 0)
					compare = 0;
				else if (thisNbPrenom == 0 && personneNbPrenom != 0)
					compare = -1;
				else if (thisNbPrenom != 0 && personneNbPrenom == 0)
					compare = -1;
				else
					compare = this.getInformation(Prenom.KEY).getFirst()
							.compareTo(personne.getInformation(Prenom.KEY).getFirst());
			}
		}
		return compare;
	}
}
