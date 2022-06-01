
# CS 242 Search UI

TBD

## Notes for Windows users

Make use of a package manager such chocolatey (https://chocolatey.org/). 

Install chocolatey using cmd.exe

    @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"

From the directory of the cloned source, run the following:
            
    cd cs242-search

    choco.exe install python
    choco.exe install pip
	python -m pip install -r requirements.txt
	python.exe -m venv venv 
	venv\Scripts\activate	
	set APP_SETTINGS=config.py
	set FLASK_DEBUG=1
	set FLASK_APP=run.py
	flask run


## Notes for Mac or Linux users

Use make to run commands. 

### Setup the python3 environment setup

    brew install python3
    make venv

### To run search web UI

    make run

