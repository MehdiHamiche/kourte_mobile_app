package com.example.kourteapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    static ArrayList<Trajet> trajets = new ArrayList<>();
    static KourteDbHelper kourteDbHelper;
// l'annotation d'une méthode public static void avec @beforeclass entraine son execution une fois avant l'une des méthodes de test de la classe
// les methodes seront executées avant celle de la classe courante
   @BeforeClass
    public static void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        kourteDbHelper = new KourteDbHelper(appContext);
        assertEquals("com.example.kourteapp", appContext.getPackageName());
    }

    // test la  fonctionnalité d'ajout de trajet dans la bdd
    @Test
    public void addition_Correcte() {
        trajets.clear();
        kourteDbHelper.lireTrajets(trajets);
        int nbTrajets = trajets.size();
        kourteDbHelper.ajouterTrajet(new Trajet("","",""));
        trajets.clear();
        kourteDbHelper.lireTrajets(trajets);
        int nbTrajetsNouveau = trajets.size();
        assertEquals("Echec d'ajout de trajet",nbTrajetsNouveau,nbTrajets+1);
    }
    // test la  fonctionnalité de suppression de trajet dans la bdd
    @Test
    public void suppression_Correcte() {
        trajets.clear();
        kourteDbHelper.lireTrajets(trajets);
        if (trajets.size()>0) {
            long id = trajets.get(trajets.size() - 1)._id;
            int nbTrajets = trajets.size();
            kourteDbHelper.supprimerTrajet(id);
            trajets.clear();
            kourteDbHelper.lireTrajets(trajets);
            int nbTrajetsNouveau = trajets.size();
            assertEquals("Echec de suppression de trajet", nbTrajetsNouveau, nbTrajets - 1);
        }
    }
    // test le  changement de l'etat d'un trajet si favorie ou non
    @Test
    public void marquage_Favorie() {
        trajets.clear();
        kourteDbHelper.lireTrajets(trajets);
        if (trajets.size()>0) {
            long id = trajets.get(0)._id;
            boolean estFavorie=trajets.get(0).favorie;
            kourteDbHelper.marquerFavorie(id,!estFavorie);
            trajets.clear();
            kourteDbHelper.lireTrajets(trajets);
            boolean estFavorieNouveau=trajets.get(0).favorie;
            assertNotEquals("Echec de changement d'etat de favorie", estFavorie, estFavorieNouveau);
        }
    }

}