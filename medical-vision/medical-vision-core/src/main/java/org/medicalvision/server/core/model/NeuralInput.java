package org.medicalvision.server.core.model;

/**
 * TOD = Time Of Day (0 = night, 1 = day)
 * TSLS = Time Since Last Signsal
 * 
 * @author sirolf2009
 *
 */
public class NeuralInput {
	
	private double TOD;
	private double motion1;
	private long motion1TSLS;
	private double motion2;
	private long motion2TSLS;
	private double infra1;
	private long infra1TSLS;
	private double infra2;
	private long infra2TSLS;
	private double door1;
	private long door1TSLS;
	private double door2;
	private long door2TSLS;
	private double light1;
	private long light1TSLS;
	private double light2;
	private long light2TSLS;
	private double panic;
	
	public NeuralInput() {
	}
	
	public double[] toArray() {
		return new double[] {TOD, motion1, motion1TSLS, motion2, motion2TSLS, infra1, infra1TSLS, infra2, infra2TSLS, door1, door1TSLS, door2, door2TSLS, light1, light1TSLS, light2, light2TSLS, panic};
	}

	public double getTOD() {
		return TOD;
	}

	public void setTOD(double tOD) {
		TOD = tOD;
	}

	public double getMotion1() {
		return motion1;
	}

	public void setMotion1(double motion1) {
		this.motion1 = motion1;
	}

	public long getMotion1TSLS() {
		return motion1TSLS;
	}

	public void setMotion1TSLS(long motion1tsls) {
		motion1TSLS = motion1tsls;
	}

	public double getMotion2() {
		return motion2;
	}

	public void setMotion2(double motion2) {
		this.motion2 = motion2;
	}

	public long getMotion2TSLS() {
		return motion2TSLS;
	}

	public void setMotion2TSLS(long motion2tsls) {
		motion2TSLS = motion2tsls;
	}

	public double getInfra1() {
		return infra1;
	}

	public void setInfra1(double infra1) {
		this.infra1 = infra1;
	}

	public long getInfra1TSLS() {
		return infra1TSLS;
	}

	public void setInfra1TSLS(long infra1tsls) {
		infra1TSLS = infra1tsls;
	}

	public double getInfra2() {
		return infra2;
	}

	public void setInfra2(double infra2) {
		this.infra2 = infra2;
	}

	public long getInfra2TSLS() {
		return infra2TSLS;
	}

	public void setInfra2TSLS(long infra2tsls) {
		infra2TSLS = infra2tsls;
	}

	public double getDoor1() {
		return door1;
	}

	public void setDoor1(double door1) {
		this.door1 = door1;
	}

	public long getDoor1TSLS() {
		return door1TSLS;
	}

	public void setDoor1TSLS(long door1tsls) {
		door1TSLS = door1tsls;
	}

	public double getDoor2() {
		return door2;
	}

	public void setDoor2(double door2) {
		this.door2 = door2;
	}

	public long getDoor2TSLS() {
		return door2TSLS;
	}

	public void setDoor2TSLS(long door2tsls) {
		door2TSLS = door2tsls;
	}

	public double getLight1() {
		return light1;
	}

	public void setLight1(double light1) {
		this.light1 = light1;
	}

	public long getLight1TSLS() {
		return light1TSLS;
	}

	public void setLight1TSLS(long light1tsls) {
		light1TSLS = light1tsls;
	}

	public double getLight2() {
		return light2;
	}

	public void setLight2(double light2) {
		this.light2 = light2;
	}

	public long getLight2TSLS() {
		return light2TSLS;
	}

	public void setLight2TSLS(long light2tsls) {
		light2TSLS = light2tsls;
	}

	public double getPanic() {
		return panic;
	}

	public void setPanic(double panic) {
		this.panic = panic;
	}

}
