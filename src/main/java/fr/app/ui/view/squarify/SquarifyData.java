package fr.app.ui.view.squarify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SquarifyData {
	private ArrayList<DataPoint> data;	// List of data points holding value, normalized value and id
	private static float totalSize;	 	// Total size of values
	private static float totalArea;	 	// Total area of canvas
	private static int length;			// Total amount of values

	public SquarifyData(ArrayList<Float> values, ArrayList<String> ids, float width, float height) {
		this.data = new ArrayList<DataPoint>();
		setValues(values, ids);
		sortDescending();

		totalSize = sumValues(this.data);
		length = values.size();
		totalArea = width * height;

		normalize();
	}

	private void sortDescending() {
		Collections.sort(this.data, new Comparator<DataPoint>() {
		    @Override
		    public int compare(DataPoint a1, DataPoint a2) {
		        return Float.compare(a1.getValue(), a2.getValue());
		    }
		});
		Collections.reverse(this.data);
	}

	private void setValues(ArrayList<Float> values, ArrayList<String> ids) {
		for (int i = 0; i < values.size(); i++) {
			data.add(new DataPoint(ids.get(i), values.get(i)));
		}
	}
	
	/**
	 * Normalize data to canvas area
	 */
	private void normalize() {
		for (int i = 0; i < this.data.size(); i++) {
			this.data.get(i).normalize();
		}
	}
	
	/** 
	 * Helper that sums all the values of DataPoint
	 * @param values A list of DataPoint values
	 * @return The sum of these values
	 */
	private float sumValues(List<DataPoint> l) {
		float result = 0;
		for (int i = 0; i < l.size(); i++) {
			result += l.get(i).getValue();
		}
		return result;
	}
	
	public float getTotalSize() {
		return totalSize;
	}
	
	public int getLength() {
		return length;
	}
	
	public ArrayList<DataPoint> getDataPoints() {
		return this.data;
	}
	
	public DataPoint getDataPoint(int i) {
		return this.data.get(i);
	}
	
	/**
	 * DataPoint holds the original size value passed as parameter to Squarify
	 * It also stores the id of the value, to keep track of it as the squarify
	 * algorithm requires to sort values descending.
	 * @author agathelenclen
	 *
	 */
	public static class DataPoint {
		private float value;
		private float normalizedValue;
		private String id;
		
		public DataPoint(String id, float value) {
			this.id = id;
			this.value = value;
		}
		
		/**
		 * Normalizes the value size to the proportion of the canvas. 
		 */
		private void normalize() {
			this.normalizedValue = this.value * totalArea / totalSize;
		}
		
		public float getValue() {
			return value;
		}
		
		public float getNormalizedValue() {
			return normalizedValue;
		}
	
		public String getId() {
			return id;
		}
	}
}
