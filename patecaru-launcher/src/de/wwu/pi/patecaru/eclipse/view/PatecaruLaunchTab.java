//package de.wwu.pi.patecaru.eclipse.view;
//
//import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
//import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationTab;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.ui.PlatformUI;
//
//public class PatecaruLaunchTab extends JUnitLaunchConfigurationTab {
//
//	
//	@Override
//	public void createControl(Composite parent) {
//		Composite comp = new Composite(parent, SWT.NONE);
//		setControl(comp);
//
//		GridLayout topLayout = new GridLayout();
//		topLayout.numColumns= 3;
//		comp.setLayout(topLayout);
//
//		createSingleTestSection(comp);
//		createSpacer(comp);
//		
//		createTestContainerSelectionGroup(comp);
//		createSpacer(comp);
//
//		createTestLoaderGroup(comp);
//		createSpacer(comp);
//
//		createKeepAliveGroup(comp);
//		Dialog.applyDialogFont(comp);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJUnitHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_JUNIT_MAIN_TAB);
//		validatePage();
//	}
//	
//	
//}
