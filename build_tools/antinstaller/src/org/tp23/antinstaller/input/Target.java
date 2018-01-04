package org.tp23.antinstaller.input;
/**
 * Indicates the input type is a target based type and requires special
 * processing in the properties loader
 * @author teknopaul
 *
 */
public interface Target {
	public int getIdx();
	public String getTarget();
}
