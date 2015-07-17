package de.dominikmuench.twintowns;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class TwinTownTable extends Table {

	private final static String DATA_FILENAME = "TwinTowns_clean.csv";
	private final static String COL_MUNICIPALITY = "municipality";
	private final static String COL_LAT = "latitude";
	private final static String COL_LNG = "longitude";
	private final static String COL_STATE = "state";
	private final static String COL_PARTNER_MUNICIPALITY = "partnergemeinde";
	private final static String COL_PARTNER_COUNTRY = "country";
	private final static String COL_PARTNER_CONTINENT = "continent";
	private final static String COL_PARTNER_LAT = "partner_latitude";
	private final static String COL_PARTNER_LNG = "partner_longitude";

	private PApplet pa;
	private Table table;

	public TwinTownTable(PApplet pa) {
		this.pa = pa;
		this.table = this.pa.loadTable(DATA_FILENAME, "header");
	}

	public Iterable<TableRow> rows() {
		return this.table.rows();
	}

	/**
	 * Gets the municipality name for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public String getMunicipality(TableRow row) {
		return row.getString(COL_MUNICIPALITY);
	}

	/**
	 * Gets the latitude for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public float getLatitude(TableRow row) {
		return row.getFloat(COL_LAT);
	}

	/**
	 * Gets the longitude for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public float getLongitude(TableRow row) {
		return row.getFloat(COL_LNG);
	}

	/**
	 * Gets the municipality's state for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public String getState(TableRow row) {
		return row.getString(COL_STATE);
	}

	/**
	 * Gets the partner municipality for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public String getPartnerMunicipality(TableRow row) {
		return row.getString(COL_PARTNER_MUNICIPALITY);
	}

	/**
	 * Gets the latitude of the partner municipality for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public float getPartnerLatitude(TableRow row) {
		return row.getFloat(COL_PARTNER_LAT);
	}

	/**
	 * Gets the longitude of the partner municipality for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public float getPartnerLongitude(TableRow row) {
		return row.getFloat(COL_PARTNER_LNG);
	}

	/**
	 * Gets the country of the partner municipality for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public String getPartnerCountry(TableRow row) {
		return row.getString(COL_PARTNER_COUNTRY);
	}

	/**
	 * Gets the continent of the partner municipality for a given row.
	 * 
	 * @param row
	 * @return
	 */
	public String getPartnerContinent(TableRow row) {
		return row.getString(COL_PARTNER_CONTINENT);
	}

}
