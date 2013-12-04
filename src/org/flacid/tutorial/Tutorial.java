package org.flacid.tutorial;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;

@SuppressWarnings("unused")
public class Tutorial implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	if (!lpparam.packageName.equals("com.android.email"))
            return;
    	
    	XposedBridge.log("i'm all up in that email");
 
    	findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "isActiveAdmin", new XC_MethodReplacement() {
    		
    		@Override
    		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				return Boolean.valueOf(false);
    		}
    	});  
    	

    	//XposedBridge.hookAllMethods(hookClass, methodName, callback)

//    	public class Policy = Class.forName("com.android.emailcommon.provider.Policy.Policy", true, lpparam.classLoader);

    	
    	findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "isActive", "com.android.emailcommon.provider.Policy", new XC_MethodReplacement() {
    		
    		@Override
    		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				return Boolean.valueOf(true);
    		}
    	});
    	
    }
}