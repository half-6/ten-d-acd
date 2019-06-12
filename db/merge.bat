set cwd=%~dp0
copy %cwd%\db.sql+%cwd%\table.sql+%cwd%\view.sql+%cwd%\data.sql %cwd%\..\docker\tend\db.sql