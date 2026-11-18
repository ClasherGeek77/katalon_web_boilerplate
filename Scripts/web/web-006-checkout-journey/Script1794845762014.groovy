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
WebUI.click(findTestObject('Object Repository/inventory/link_cart'))
WebUI.verifyElementText(findTestObject('Object Repository/cart/txt_item_name'), 'Sauce Labs Backpack')

WebUI.click(findTestObject('Object Repository/cart/button_checkout'))
WebUI.setText(findTestObject('Object Repository/checkout/input_first_name'), 'Auto')
WebUI.setText(findTestObject('Object Repository/checkout/input_last_name'), 'Test')
WebUI.setText(findTestObject('Object Repository/checkout/input_zip'), '12345')
WebUI.click(findTestObject('Object Repository/checkout/button_continue'))

// the clone's own math: 29.99 + 8% tax (2.40), delivery free
WebUI.verifyElementText(findTestObject('Object Repository/checkout/txt_total'), 'Total: $32.39')

WebUI.click(findTestObject('Object Repository/checkout/button_finish'))
WebUI.verifyElementText(findTestObject('Object Repository/checkout/txt_complete'), 'Thank you for your order!')

WebUI.closeBrowser()
