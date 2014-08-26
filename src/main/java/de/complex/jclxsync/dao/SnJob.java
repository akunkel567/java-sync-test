/*
 * SnJob.java
 *
 * Created on 4. Juli 2006, 09:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.complex.jclxsync.dao;

/**
 *
 * @author kunkel
 */
public class SnJob {

   private int snJobId;
   private String eventName;
   private int snJobFremdId;
   private String snJobTyp;
   private String fileName;
   private String herkunft;
	private String sau;
   private String sadt;

	public String getSadt() {
		return sadt;
	}

	public void setSadt(String sadt) {
		this.sadt = sadt;
	}

	public String getSau() {
		return sau;
	}

	public void setSau(String sau) {
		this.sau = sau;
	}

	public String getScdt() {
		return scdt;
	}

	public void setScdt(String scdt) {
		this.scdt = scdt;
	}

	public String getScu() {
		return scu;
	}

	public void setScu(String scu) {
		this.scu = scu;
	}
   private String scu;
   private String scdt;

   /** Creates a new instance of SnJob */
   public SnJob() {
   }


   /** Creates a new instance of SnJob */
   public SnJob(int snJobId, String eventName, int snJobFremdId, String snJobTyp, String fileName, String herkunft) {
      this.setSnJobId(snJobId);
      this.setEventName(eventName);
      this.setSnJobFremdId(snJobId);
      this.setSnJobTyp(snJobTyp);
      this.setFileName(fileName);
      this.setHerkunft(herkunft);
   }

	public int getSnJobFremdId() {
		return snJobFremdId;
	}

	public void setSnJobFremdId(int snJobFremdId) {
		this.snJobFremdId = snJobFremdId;
	}

	public int getSnJobId() {
		return snJobId;
	}

	public void setSnJobId(int snJobId) {
		this.snJobId = snJobId;
	}

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getEventName() {
      return this.eventName;
   }

   public void setSnJobTyp(String snJobTyp) {
      this.snJobTyp = snJobTyp;
   }

   public String getSnJobTyp() {
      return this.snJobTyp;
   }

   public String getHerkunft() {
      return this.herkunft;
   }

    public void setHerkunft(String herkunft) {
      this.herkunft = herkunft;
   }

	@Override
	public String toString() {
		return "SnJob {snjobid " + snJobId + ", eventName " + eventName + ", snJobFremdId " + snJobFremdId + ", snJobTyp " + snJobTyp + ", fileName " + fileName + "}";
	}
}



