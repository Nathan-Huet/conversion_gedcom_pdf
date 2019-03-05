package controleur;

import exception.DejaDansFamilleException;
import exception.FamilleExistanteException;
import exception.FamilleNonExistanteException;
import modele.Famille;
import modele.FamilleConcrete;
import modele.Livre;
import modele.Personne;
import modele.PersonneConcrete;
import modele.infos.Evenement;
import modele.infos.Fichier;
import modele.infos.Genre;
import modele.infos.Lien;
import modele.infos.Nom;
import modele.infos.Note;
import modele.infos.Prenom;
import modele.infos.Profession;
import modele.infos.Surnom;
import vue.Fenetre;

public class ControleurConcret extends Controleur {

	/**
	 * constructeur de la classe Controleur
	 * 
	 * @param livre livre sur lequel les modifications vont etre apportees
	 */
	public ControleurConcret(Livre livre, Fenetre fenetre) {
		super(fenetre, livre);
		this.livre = livre;
		this.fenetre = fenetre;
	}

	/**
	 * modifie les caracteristiques d'un individu (nom de famille, prenom, genre,
	 * surnom, profession, note) ajoute l'individu à la famille parental lorsque la
	 * chaine est "1 FAMC numero de la famille parental" (1 peut etre remplacee par
	 * une chaine de caracteres ne comprenant pas d'espaces))
	 * 
	 * @param individu         De la classe Personne, l'individu dont une
	 *                         caractéristique va etre modifiee
	 * @param varLecteur2Split tableau de chaine de caracteres contenant en case 1
	 *                         (2 eme case du tableau) la chaine permettant de
	 *                         savoir quelle caracteristique modifier
	 * @throws DejaDansFamilleException     propage une exception si on essaye
	 *                                      d'ajouter un individu dans une famille
	 *                                      alors que le numero de l'individu est
	 *                                      deja dans la famille (soit en tant que
	 *                                      parent soit en tant qu'enfant)
	 * @throws FamilleNonExistanteException propage une exception si on essaye
	 *                                      d'obtenir une Famille en utilisant son
	 *                                      numero et que celle-ci n'est pas
	 *                                      contenue dans le Livre
	 */
	public void modifierIndividu(Personne individu, String[] varLecteur2Split)
			throws DejaDansFamilleException, FamilleNonExistanteException {
		String texte = "";
		Famille familleIndividu;

		for (int i = 2; i < varLecteur2Split.length; i++) {
			texte += varLecteur2Split[i] + " ";
		}
		if (texte.contains("http://") || texte.contains("https://")) {
			individu.ajouterInformation(Lien.KEY, texte);
		}
		switch (varLecteur2Split[1]) {
		case "NAME":
			break;
		case "SURN":
			individu.ajouterInformation(Nom.KEY, texte);
			break;
		case "GIVN":
			individu.ajouterInformation(Prenom.KEY, texte);
			break;
		case "SEX":
			individu.ajouterInformation(Genre.KEY, texte);
			break;
		case "NICK":
			individu.ajouterInformation(Surnom.KEY, texte);
			break;
		case "NOTE":
			individu.ajouterInformation(Note.KEY, texte);
			break;
		case "PROF":
			individu.ajouterInformation(Profession.KEY, texte);
			break;
		case "FAMC":
			familleIndividu = new FamilleConcrete(texte);
			try {
				livre.ajouterFamille(familleIndividu);
			} catch (FamilleExistanteException e) {
				// System.out.println("Cette famille est déjà dans le livre");
				// Exception inutile ici peut-etre la remplacee par une condition?
				// Le try catch permet de creer la famille la premiere fois que le numero de
				// celle-ci est trouve
				// et de l'ignorer les fois suivantes
			} finally {
				livre.getFamille(texte).ajouterEnfant(individu);
				// Exception FamilleNonExistanteException jamais rencontree, ici ne peut pas
				// etre rencontree
				// car la famille n'est pas creee et ajoutee au livre seulement si elle est deja
				// dedans
			}
			break;
		case "FAMS":
			familleIndividu = new FamilleConcrete(texte);
			try {
				livre.ajouterFamille(familleIndividu);
			} catch (FamilleExistanteException e) {
				// System.out.println("Cette famille existe déjà");
				// Exception inutile ici peut-etre la remplacee par une condition?
				// Le try catch permet de creer la famille la premiere fois que le numero de
				// celle-ci est trouve
				// et de l'ignorer les fois suivantes
			} finally {
				if (livre.getFamille(texte).getParent_1().getNum().equals("inconnu")) {
					livre.getFamille(texte).setParent_1(individu);
				} else if (livre.getFamille(texte).getParent_2().getNum().equals("inconnu")) {
					livre.getFamille(texte).setParent_2(individu);
				}
				// Exception FamilleNonExistanteException jamais rencontree, ici ne peut pas
				// etre rencontree
				// car la famille n'est pas creee et ajoutee au livre seulement si elle est deja
				// dedans
			}
			break;
		/*case "SOUR":
			individu.ajouterInformation(Lien.KEY, texte);
			break;*/
		case "BIRT":
			individu.ajouterInformation(Evenement.KEY, varLecteur2Split[1]);
			break;
		case "DEAT":
			individu.ajouterInformation(Evenement.KEY, varLecteur2Split[1]);
			break;
		case "EVEN":
			individu.ajouterInformation(Evenement.KEY, varLecteur2Split[1]);
			break;
		case "DATE":
			individu.ajouterInformation(Evenement.KEY, varLecteur2Split[1] + " " + texte);
			break;
		case "PLAC":
			individu.ajouterInformation(Evenement.KEY, varLecteur2Split[1] + " " + texte);
			break;
		case "FILE":
			individu.ajouterInformation(Fichier.KEY, individu.getNum());
			break;
		default:
			// individu.ajouterInformation(Evenement.KEY, texte);
			// rajouter d'autres case et attributs a la classe Personne(date de
			// naissance...)
			break;
		}
		
	}

	/**
	 * methode renvoyant une Personne cree a partir d'une chaine de caractere
	 * 
	 * @param numIndividu numero de l'individu cree (unique dans le fichier) CF
	 *                    methode equals de la classe Personne
	 * @return individu cree
	 */
	public Personne creerIndividu(String numIndividu) {
		Personne individu;
		individu = new PersonneConcrete(numIndividu);
		return individu;
	}
}
