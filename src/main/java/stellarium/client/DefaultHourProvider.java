package stellarium.client;

import akka.actor.ActorSystem.Settings;
import stellarium.StellarSky;
import stellarium.api.IHourProvider;
import stellarium.util.math.StellarMath;

public class DefaultHourProvider implements IHourProvider {
	
	private ClientSettings setting;
	
	public DefaultHourProvider(ClientSettings settings) {
		this.setting = settings;
	}

	@Override
	public int getCurrentHour(double dayLength, double timeInDay) {
		return (int)Math.floor(timeInDay / (setting.minuteLength * setting.anHourToMinute));
	}

	@Override
	public int getCurrentMinute(double dayLength, double timeInDay, int hour) {
		return (int)Math.floor(timeInDay / setting.minuteLength) - hour * setting.anHourToMinute;
	}

	@Override
	public int getTotalHour(double dayLength) {
		return (int)Math.floor(dayLength / (setting.minuteLength * setting.anHourToMinute));
	}

	@Override
	public int getTotalMinute(double dayLength, int totalHour) {
		return setting.anHourToMinute;
	}

	@Override
	public int getRestMinuteInDay(double daylength, int totalHour) {
		return (int) Math.floor(daylength / setting.minuteLength - totalHour * setting.anHourToMinute);
	}

}
