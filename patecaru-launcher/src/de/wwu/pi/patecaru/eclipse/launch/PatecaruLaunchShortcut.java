package de.wwu.pi.patecaru.eclipse.launch;

import org.eclipse.pde.ui.launcher.JUnitWorkbenchLaunchShortcut;

public class PatecaruLaunchShortcut extends JUnitWorkbenchLaunchShortcut {
	
	@Override
	protected String getLaunchConfigurationTypeId() {
		return "de.wwu.pi.patecaru.eclipse.launchConfigurationType";
	}
}
