set cwd=%~dp0
type %cwd%db.sql  %cwd%table.sql  %cwd%view.sql  %cwd%data.sql > %cwd%..\docker\tend\db.sql