import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.maximizeWindow()
WebUI.navigateToUrl(GlobalVariable.BASE_URL.toString())

WebUI.setText(findTestObject('Object Repository/login/input_username'), 'standard_user')
WebUI.click(findTestObject('Object Repository/login/button_login'))

WebUI.verifyElementText(findTestObject('Object Repository/login/txt_error'), 'Epic sadface: Password is required')

WebUI.closeBrowser()
