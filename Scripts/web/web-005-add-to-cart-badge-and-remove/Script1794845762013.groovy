import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.maximizeWindow()
WebUI.navigateToUrl(GlobalVariable.BASE_URL.toString())

WebUI.setText(findTestObject('Object Repository/login/input_username'), 'standard_user')
WebUI.setText(findTestObject('Object Repository/login/input_password'), 'secret_sauce')
WebUI.click(findTestObject('Object Repository/login/button_login'))

WebUI.click(findTestObject('Object Repository/inventory/button_add_backpack'))
WebUI.verifyElementText(findTestObject('Object Repository/inventory/span_cart_badge'), '1')

WebUI.click(findTestObject('Object Repository/inventory/button_remove_backpack'))
WebUI.verifyElementNotPresent(findTestObject('Object Repository/inventory/span_cart_badge'), 0)

WebUI.closeBrowser()
