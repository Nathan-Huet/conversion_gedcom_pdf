package controleur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

import exception.AucunFichierSelectionneException;
import exception.DejaDansFamilleException;
import exception.FamilleExistanteException;
import exception.FamilleNonExistanteException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import modele.Livre;
import modele.Personne;
import vue.Fenetre;

public abstract class Controleur {
	protected File fichier;
	protected FileInputStream fis = null;

	protected Fenetre fenetre;
	protected Livre livre;

	/**
	 * constructeur de la classe Controleur
	 * 
	 * @param livre livre sur lequel les modifications vont etre apportees
	 */
	public Controleur(Fenetre fenetre, Livre livre) {
		this.fenetre = fenetre;
		this.livre = livre;
	}

	/**
	 * Permet d'afficher le contenu du fichier dans la console
	 * 
	 * @param fichier fichier à lire
	 * @throws IOException Exception signalant qu'une erreur d'entree/sortie a eu
	 *                     lieu. Cette classe est la classe generale des exceptions
	 *                     produites par des operations d'entree/sortie interrompues
	 *                     ou echouees. (source: javadoc IOException)
	 */
	public String lireFichier(File fichier) throws IOException {
		BufferedReader lecteurAvecBuffer = null;
		BufferedReader lecteurAvecBuffer2 = null;
		String contenu = "";

		fis = new FileInputStream(fichier);

		lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
		lecteurAvecBuffer2 = new BufferedReader(new FileReader(fichier));

		System.out.println("Nom du fichier: " + fichier.getName());
		while (lecteurAvecBuffer.readLine() != null) {
			contenu += lecteurAvecBuffer2.readLine() + "\n";
		}
		lecteurAvecBuffer.close();
		lecteurAvecBuffer2.close();
		fis.close();
		System.out.println(contenu);
		return contenu;
	}

	/**
	 * enregistre les personnes du fichier dans le livre
	 * 
	 * @param fichier fichier GEDCOM a traiter
	 * @throws FamilleExistanteException        Exception signalant qu'une Famille
	 *                                          etait deja presente dans le Livre
	 *                                          lorsqu'on a tente de l'ajouter
	 * @throws DejaDansFamilleException         Exception signalant qu'une Personne
	 *                                          etait deja presente dans une Famille
	 *                                          lorsqu'on a tente de l'ajouter
	 * @throws FamilleNonExistanteException     Exception signalant qu'une Famille
	 *                                          cherchee n'est pas presente dans le
	 *                                          Livre
	 * @throws IOException                      Exception signalant qu'une erreur
	 *                                          d'entree/sortie a eu lieu. Cette
	 *                                          classe est la classe generale des
	 *                                          exceptions produites par des
	 *                                          operations d'entree/sortie
	 *                                          interrompues ou echouees. (source:
	 *                                          javadoc IOException)
	 * @throws AucunFichierSelectionneException Exception signalant qu'il n'y a pas
	 *                                          de fichier selectionne
	 */
	public void enregistrerIndividus(File fichier) throws IOException, DejaDansFamilleException,
			FamilleExistanteException, FamilleNonExistanteException, AucunFichierSelectionneException {
		if (fichier == null)
			throw new AucunFichierSelectionneException("Le fichier gedcom a convertir n'a pas etait selectionne");

		System.out.println("Nom du fichier: " + fichier.getName());

		BufferedReader lecteurAvecBuffer = null;
		BufferedReader lecteurAvecBuffer2 = null;
		fis = new FileInputStream(fichier);
		lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
		lecteurAvecBuffer2 = new BufferedReader(new FileReader(fichier));

		ArrayList<Personne> toutesPersonne = new ArrayList<Personne>();

		Personne individu = null;
		boolean verifIndividu = false;

		// varlecteur2 permet de stocker la chaine de caractères obtenue
		// avec lecteurAvecBuffer2.readline()
		String varLecteur2;

		// varLecteur2Split permet de stocker varlecteur2.split("\\s+")
		// afin de pouvoir retrouver les mots-clés du GEDCOM (INDI, FAMC, GIVN,...)
		// doit être initialisé avec
		// varLecteur2Split = new String[varlecteur2.split("\\s+").length];
		String[] varLecteur2Split;

		// permet de lire le fichier
		// 2 lecteur avec Buffer un pour verifier
		// qu'on n'a pas atteint la fin du fichier
		// et l'autre pour le traitement (pas sur que ce soit utile)
		while ((lecteurAvecBuffer.readLine()) != null) {
			varLecteur2 = lecteurAvecBuffer2.readLine();
			varLecteur2Split = new String[varLecteur2.split("\\s+").length];
			varLecteur2Split = varLecteur2.split("\\s+");
			// System.out.println(varLecteur2Split[0]);

			// vérifie que la première lettre de la ligne est un 0
			if (varLecteur2Split[0].contentEquals("0")) {
				// vérifie que le tableau fait au moins 3 cases
				if (varLecteur2Split.length >= 3) {
					// vérifie que c'est un individu
					if (varLecteur2Split[2].contentEquals("INDI")) {
						// on cree un nouvel individu lorsque la ligne
						// est ecrite ainsi "0 *....* *INDI*"
						// ( avec *....* en tant que numero d'individu)
						individu = creerIndividu(varLecteur2Split[1]);

						// On ajoute tous les individus a la liste toutesPersonne
						// afin de pouvoir facilement traiter les cas des personnes
						// dont on ne possede pas le numero de la famille parental
						// (Pas dans le fichier)
						toutesPersonne.add(individu);

						// le booleen verifIndividu permet de modifier un individu
						// CF modifierIndividu(...) lorsqu'on a verifier que les lignes
						// suivantes du fichier correspondent bien aux informations le concernant
						verifIndividu = true;

					} else {
						// Si la ligne est ecrite ainsi "0 *....* *PAS.INDI*" (commence par un 0
						// et contient deux autres chaines de caracteres separee par des espaces
						// et ne contenant pas la chaine de caractere INDI en troisieme chaine)
						verifIndividu = false;
					}
				} else {
					// Si la ligne contient moins de trois chaines de carateres separees par des
					// espaces
					verifIndividu = false;
				}
			} else {
				if (verifIndividu) {
					modifierIndividu(individu, varLecteur2Split);
				}
			}
		}
		// traitement des cas des personnes dont on ne connait pas le numero de famille
		// parental
		// parcour de la liste contenant toutes les Personne du fichier
		// verification pour chaque Personne que la methode permettant de recuperer
		// la famille parental ne renvoie pas l'exception FamilleNonExistanteException
		// dans le cas ou l'exception est attrapee dans le try catch
		// ajouter la Personne a la famille "inconnu"
		for (Personne personne : toutesPersonne) {
			try {
				livre.getFamilleParental(personne);
			} catch (FamilleNonExistanteException e) {
				livre.getFamille("inconnu").ajouterEnfant(personne);
			}
		}
		lecteurAvecBuffer.close();
		lecteurAvecBuffer2.close();

		livre.trier(toutesPersonne);
	}

	/**
	 * methode renvoyant une Personne cree a partir d'une chaine de caractere
	 * 
	 * @param numIndividu numero de l'individu cree (unique dans le fichier) CF
	 *                    methode equals de la classe Personne
	 * @return individu cree
	 */
	public abstract Personne creerIndividu(String numPersonne);

	/**
	 * methode permettant de quitter le programme
	 */
	public void quitter() {
		System.exit(0);
	}

	public void aPropos() {
		File fichierAPropos = new File("A_Propos.txt");
		String aPropos = "";
		try {
			aPropos = lireFichier(fichierAPropos);
		} catch (IOException e) {
			fenetre.zoneDeTexte(e.getMessage());
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("A propos");
		alert.setHeaderText(null);
		alert.setContentText(aPropos);

		alert.showAndWait();
	}

	/**
	 * methode permettant de selectionner le fichier gedcom (ouvre une fenetre
	 * permettant de selectionner le fichier parmi ceux ayant l'extension .ged)
	 */
	public void selectionner() {
		FileChooser fichierChoisir = new FileChooser();
		fichierChoisir.getExtensionFilters().addAll(new ExtensionFilter("Fichiers gedcom", "*.ged"));
		fichier = fichierChoisir.showOpenDialog(null);
	}

	/**
	 * methode permettant de generer le fichier PDF appelle les methode permettant
	 * d'enregistrer les individus, le pdf et de creer le pdf
	 */
	public void genererPDF() {
		if (!livre.getListeDesFamilles().isEmpty()) {
			livre.initialiserLivre();
		}
		try {
			enregistrerIndividus(fichier);
			enregistrerSous();
			livre.creerPDF();
		} catch (IOException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		} catch (DejaDansFamilleException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		} catch (FamilleExistanteException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		} catch (FamilleNonExistanteException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		} catch (AucunFichierSelectionneException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		} catch (DocumentException e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			fenetre.zoneDeTexte(e.getMessage());
			e.printStackTrace();
		}
		fenetre.zoneDeTexte("Fini");
	}

	/**
	 * methode permettant de choisir le modele du Livre
	 */
	public void choisirModele() {
		DirectoryChooser dossierChoisir = new DirectoryChooser();
		File fichierChoisi = dossierChoisir.showDialog(null);

		if (fichierChoisi != null && fichierChoisi.isDirectory()) {
			livre.image = fichierChoisi.getAbsolutePath() + "/photos/";
			livre.temp = fichierChoisi.getAbsolutePath() + "\\temporaire.pdf";
			livre.modele = fichierChoisi.getAbsolutePath() + "\\modele.pdf";
			livre.page_gauche = fichierChoisi.getAbsolutePath() + "\\page_gauche.jpg";
			livre.page_droite = fichierChoisi.getAbsolutePath() + "\\page_droite.jpg";
			livre.page_couv_gauche = fichierChoisi.getAbsolutePath() + "\\page_couv_gauche.jpg";
			livre.page_couv_droite = fichierChoisi.getAbsolutePath() + "\\page_couv_droite.jpg";
			livre.lien_page_suivante = fichierChoisi.getAbsolutePath() + "\\lien_page_suivante.jpg";
			livre.lien_page_precedente = fichierChoisi.getAbsolutePath() + "\\lien_page_precedente.jpg";

		}
	}

	/**
	 * methode permettant de choisir la destination du pdf
	 * 
	 * @throws AucunFichierSelectionneException Exception signalant qu'il n'y a pas
	 *                                          de fichier selectionne
	 */
	public void enregistrerSous() throws AucunFichierSelectionneException {
		FileChooser fichierChoisir = new FileChooser();
		fichierChoisir.getExtensionFilters().addAll(new ExtensionFilter("Fichiers pdf", "*.pdf"));
		File fichierChoisi = fichierChoisir.showSaveDialog(null);
		if (fichierChoisi == null)
			throw new AucunFichierSelectionneException(
					"La destination d'enregistrement du fichier pdf n'a pas ete definie");
		livre.pdf = fichierChoisi.getAbsolutePath();
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
	public abstract void modifierIndividu(Personne individu, String[] varLecteur2Split)
			throws DejaDansFamilleException, FamilleNonExistanteException;
}
