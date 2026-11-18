# katalon_web_boilerplate

A Katalon Studio web test project: 6 browser tests of a login, cart and checkout flow, written as plain built-in `WebUI.*` steps against locators stored in the Object Repository. It tests a local copy of the saucedemo shop, so it needs no internet and no account on a public site.

The `legacy` branch holds the full history of the older predecessor repos (`katalon_web_automation_framework`, `early_katalon_automation_test`).

## when to use it

Use it to learn or start a Katalon web project: Object Repository with xpath locators, test cases, a test suite, a global-variable profile.

Do not use it if:

- you want a code-first Python framework with pytest: use https://github.com/ClasherGeek77/pytest_web_boilerplate (same shop, no Katalon licence needed)
- you want to test an API: use https://github.com/ClasherGeek77/katalon_api_boilerplate
- you want to test a mobile app: use https://github.com/ClasherGeek77/katalon_android_boilerplate or https://github.com/ClasherGeek77/katalon_ios_boilerplate

## what you need

| need | detail |
|---|---|
| Katalon Studio 7.9.1 | the version recorded in `.classpath`; the project was created with it. Download from https://katalon.com/download (older versions are listed on Katalon's release pages). On macOS 10.15, 7.9.1 is the build to use. On a newer OS a current Studio should open and upgrade the project (not verified). |
| Katalon account / licence | Studio asks you to sign in. Running from the command line needs Katalon Runtime Engine, a licensed product; check the current terms on https://katalon.com. Not verified here. |
| Google Chrome | any current Chrome. Studio normally fetches a matching chromedriver itself when it is online. |
| Python 3.8+ | only to run the saucedemo clone (standard library, no pip install) |
| git | to clone the repos |

For the command-line path (`./run_katalon.sh`) you also need a `katalon` console launcher, see "run from the command line" below.

## quick start

Start the target app, the local saucedemo clone, in one terminal and leave it running.

```bash
git clone https://github.com/ClasherGeek77/saucedemo_clone.git
cd saucedemo_clone
python3 server.py            # http://localhost:50000   (python3 server.py --port 8080 for another port)
```

Check it: open http://localhost:50000 in a browser. You should see the Swag Labs login page. `standard_user` / `secret_sauce` logs in.

Get this project:

```bash
git clone https://github.com/ClasherGeek77/katalon_web_boilerplate.git
```

### run in Katalon Studio (the path that needs no scripts)

1. Open Katalon Studio and sign in.
2. File > Open Project, pick the cloned folder (it contains `katalon_boilerplate_web.prj`).
3. In the Tests Explorer open `Test Suites/web/web_smoke`.
4. Choose Chrome in the run button's dropdown and run it.

You should see 6 test cases run one after the other, each opening and closing a Chrome window, and the suite end with 6 passed, 0 failed. Reports land in `Reports/` (git-ignored).

### run from the command line

```bash
./run_katalon.sh                              # runs Test Suites/web/web_smoke
./run_katalon.sh "Test Suites/web/web_smoke"  # same, explicit
KATALON_BIN=/path/to/your/launcher ./run_katalon.sh
```

What the script does (it is about 30 lines, read it):

- changes into the project folder, then calls `$KATALON_BIN <project dir> -testSuitePath=<suite> -executionProfile=default -browserType=Chrome -retry=0 -data ~/.katalon_console/ws-web`
- `KATALON_BIN` defaults to `~/.local/bin/katalon`. That launcher is not part of this repo. It must accept the project directory as its first argument followed by the options above, and start Katalon in console mode.
- with only the stock `katalonc` from Katalon Runtime Engine, put a small wrapper around it that adds `-runMode=console`, `-projectPath=<dir>` and your `-apiKey`. Not verified with the stock binary.
- optional `KATALON_WATCH`: path to an executable that takes the log path as its argument; the script runs it in the background for live progress.
- the log goes to `~/katalon_cli_runs/web-<timestamp>.log`; the script prints the `Test Suites:` / `Test Cases:` / `FAILED` / `ERROR` lines and exits with Katalon's exit code.

You should see the last line `GREEN: katalonc exited 0, report in Reports/`, and a report at `Reports/<timestamp>/web/web_smoke/<timestamp>/JUnit_Report.xml` with `tests="6" failures="0"`. A failing run prints `RED: katalonc exit <n>, full log: ...`.

Offline, Katalon cannot download a chromedriver. If your launcher runs offline, put a chromedriver that matches your Chrome at `Drivers/chromedriver` (git-ignored) and at `~/.katalon/drivers/<full chrome version>/chromedriver`; the directory name must equal what `chrome --version` prints. If you are on macOS 10.15, the last Chrome that runs is 128, so use Chrome 128 with ChromeDriver 128.

## the 6 tests

| test case | what it checks |
|---|---|
| `web-001-standard-user-sees-products` | login works, page title is `Products` |
| `web-002-wrong-password-is-refused` | error `Epic sadface: Username and password do not match any user in this service` |
| `web-003-empty-username-is-refused` | error `Epic sadface: Username is required` |
| `web-004-empty-password-is-refused` | error `Epic sadface: Password is required` |
| `web-005-add-to-cart-badge-and-remove` | cart badge shows `1` after add, is gone after remove |
| `web-006-checkout-journey` | add backpack, checkout, total reads `Total: $32.39` (29.99 plus 8% tax, free delivery), finish shows `Thank you for your order!` |

Naming: test cases are `web-<number>-<what happens, in words>`. The suite is `web_smoke` (area, then purpose). Folders mirror the area: `Test Cases/web/`, `Scripts/web/`, `Test Suites/web/`.

## how to read the code

```
Object Repository/{login,inventory,cart,checkout}/*.rs   18 web objects, one per screen element
Test Cases/web/*.tc                                      the test case entries (name + id only)
Scripts/web/<test case>/Script<timestamp>.groovy         the steps of each test case
Test Suites/web/web_smoke.ts                             the ordered list of the 6 test cases
Profiles/default.glbl                                    global variables; BASE_URL = http://localhost:50000
settings/internal/                                       Studio settings; analytics off, captured-object selector = XPATH
run_katalon.sh                                           command-line runner
```

Locators are xpath, and they live in the Object Repository, not in the scripts. Open `Object Repository/login/input_username.rs`: the xpath is `//input[@data-test='username']`. A script only names the object, `findTestObject('Object Repository/login/input_username')`. When the site changes a locator you edit one `.rs` file (in Studio's Object Repository view) and every test that uses it is fixed. Most locators use the app's own `data-test` attribute, which is steadier than position or text.

A script is a list of built-in keywords, no custom code (the `Keywords/` folder is empty):

```groovy
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.BASE_URL.toString())
WebUI.setText(findTestObject('Object Repository/login/input_username'), 'standard_user')
WebUI.click(findTestObject('Object Repository/login/button_login'))
WebUI.verifyElementText(findTestObject('Object Repository/inventory/txt_title'), 'Products')
WebUI.closeBrowser()
```

To point the tests at another copy of the app, change `BASE_URL` in `Profiles/default.glbl` (Studio: Profiles > default).

Debugging a failure: run one test case at a time. The console has no single-test flag, so make a one-test suite: duplicate `web_smoke` in Studio, delete all but one test case link, run that. Run the full suite last.

Not included on purpose: Cucumber features, data-driven CSV binding, screenshot-on-failure wrappers, TestOps.

## if it breaks

| symptom | cause | fix |
|---|---|---|
| every test fails at `navigateToUrl` | the saucedemo clone is not running | start it (`python3 server.py` in `saucedemo_clone`), check http://localhost:50000 |
| `./run_katalon.sh`: no such file or directory | no launcher at `~/.local/bin/katalon` | set `KATALON_BIN` to your launcher, or run in Studio |
| the run opens a GUI window or waits on a login dialog | the launcher did not select console mode, or Katalon is not licensed/signed in | use a launcher that starts console mode; sign in / license Studio first |
| browser does not start, driver version message | chromedriver does not match Chrome | update Chrome or the driver; offline, stage the driver as described above |
| Studio shows missing-jar or wrong-path warnings after opening | `.classpath` and `console.properties` hold the install path of whoever last saved the project | let Studio regenerate them (reopen the project); do not commit that machine-specific diff |
| a blocking native dialog in console runs | stale workspace index in `~/.katalon_console/ws-web` | delete that folder and rerun (the script already uses a workspace of its own per project) |
| `Reports/` is empty after a red run | Katalon failed before the suite started | read the log path the script printed |

## next

- API tests in the same Katalon style: https://github.com/ClasherGeek77/katalon_api_boilerplate
- the same shop in pytest: https://github.com/ClasherGeek77/pytest_web_boilerplate
- the target app: https://github.com/ClasherGeek77/saucedemo_clone
