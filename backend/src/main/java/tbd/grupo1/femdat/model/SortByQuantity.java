package tbd.grupo1.femdat.model;
import java.util.*;

class SortByQuantity implements Comparator<Data> 
{
	/**
	 * Order desc
	 */
    public int compare(Data a, Data b) 
    { 
        return (int) (b.getQuantity() - a.getQuantity());
    } 
} 