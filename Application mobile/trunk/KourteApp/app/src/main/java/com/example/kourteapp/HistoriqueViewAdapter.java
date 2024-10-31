package com.example.kourteapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Cette classe permet de gérer l’affichage de la liste des tracés dans
 * l’historique et dans les favoris. Cette classe permet aussi de supprimer
 * un tracé des favoris.
 *
 * @author Thierno Bah
 * @version 1.0
 */
public class
HistoriqueViewAdapter extends ArrayAdapter<Trajet> implements Filterable {

	//Les attributs de la classe
	private ArrayList<Trajet> tousTrajets;
	private ArrayList<Trajet> model;


	/**
	 * Constructeur de la classe
	 *
	 * @param context
	 * @param tousTrajets
	 */
	public HistoriqueViewAdapter(Context context, ArrayList<Trajet> tousTrajets) {
		super(context, android.R.layout.simple_list_item_1,tousTrajets);
		this.tousTrajets=new ArrayList<>();
		this.tousTrajets.addAll(tousTrajets);
		this.model=tousTrajets;
	}


	/**
	 * Permet de supprimer un point
	 *
	 * @param position
	 */
	public void remove(int position){
		model.remove(position);
		notifyDataSetChanged();
	}


	/**
	 * Permet de récupérer un view
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return View
	 */
	public View getView(int position, View convertView, ViewGroup parent){
		View row=convertView;
		Trajet trajet;
		ImageView iv;
		ImageButton ib;

		if (row==null){
			LayoutInflater inflater = ((Activity)super.getContext()).getLayoutInflater();
			row = inflater.inflate(R.layout.row_historique,parent, false);
		}

		trajet = getItem(position);
		((Button)row.findViewById(R.id.row_delete)).setTag(new Integer(position));
		ib = ((ImageButton)row.findViewById(R.id.row_favorie));
		ib.setTag(new Integer(position));



		((TextView)row.findViewById(R.id.row_date)).setText(trajet.date_debut);
		((TextView)row.findViewById(R.id.row_moyen)).setText(trajet.mode_de_transport);
		((TextView)row.findViewById(R.id.row_nBPoints)).setText(trajet.nbPointsDb+"");


		iv = (ImageView)row.findViewById(R.id.row_icon);
		iv.setImageURI(Uri.parse("file://"+trajet.fileName));
		iv.setTag(new Integer(position));

		if (trajet.favorie) ib.setImageResource(R.drawable.favorites_on);
		else ib.setImageResource(R.drawable.favorites_off);

		return row;
	}


	/**
	 * Cette méthode permet de récupérer un filtre
	 *
	 * @return Filter()
	 */
	public Filter getFilter(){

		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				ArrayList<Trajet> filteredList = new ArrayList<>();
				if (constraint == null || constraint.length() == 0) {
					filteredList.addAll(tousTrajets);
				} else {
					for (Trajet trajet : model) {
						if (trajet.favorie) {
							filteredList.add(trajet);
						}
					}
				}
				FilterResults results = new FilterResults();
				results.values = filteredList;
				return results;			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				model.clear();
				model.addAll((ArrayList) filterResults.values);
				notifyDataSetChanged();
			}
		};
	}

}