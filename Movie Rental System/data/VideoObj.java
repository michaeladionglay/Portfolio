package package_hw8.data;

import package_hw8.data.VideoObj;

/**
 * Implementation of Video interface.
 * @see Data
 */
final class VideoObj implements Video {
  private final String _title;
  private final int    _year;
  private final String _director;

  /**
   * Initialize all object attributes.
   */
  VideoObj(String title, int year, String director) {
	  if ( 		(title == null) 
				|| (director == null) 
				|| (year <= 1800) 
				|| (year >= 5000)) {
			throw new IllegalArgumentException();
		}
	    this._title = title.trim();
	    this._year = year;
	    this._director = director.trim();
	    
	    
	    if (		("".equals(_title))
	    		||	("".equals(_director)))
	    	throw new IllegalArgumentException();
	    }

  public String director() {
    return _director;
  }

  public String title() {
    return _title;
  }

  public int year() {
    return _year;
  }
  
@Override
  public boolean equals(Object thatObject) {
	  if (thatObject == null) return false;
	    if (!(this.getClass().equals(thatObject.getClass())))
	      return false;
	    VideoObj that = (VideoObj) thatObject;
	    if (!this._title.equals(that.title())) return false;
		if (this._year != that.year()) return false;
		if (!this._director.equals(that.director())) return false;
	    return true;
  }

@Override
  public int hashCode() {
	  int result = 17;
	  result = 37*result + _title.hashCode();
	  result = 37*result + _year;
	  result = 37*result + _director.hashCode();
	  return result;
  }

  @Override
  public int compareTo(Object thatObject) {
	  	if(thatObject.getClass() != this.getClass()) {
	  		throw new ClassCastException();
	  	}
		VideoObj that = (VideoObj) thatObject;
		int title_Diff = _title.compareTo(that.title());
		if (title_Diff != 0) {
			return title_Diff;
		}
		int year_Diff = Integer.compare(_year, that.year());
		if (year_Diff != 0) {
			return year_Diff;
		}
		int director_Diff = _director.compareTo(that.director());
		if (director_Diff != 0) {
			return director_Diff;
		}
		return 0;
  }

  public String toString() {
	  StringBuffer tyd = new StringBuffer();
	    tyd.append(_title);
	    tyd.append(" (");
	    tyd.append(_year);
	    tyd.append(") : ");
	    tyd.append(_director);

	    return tyd.toString();
  }
}
