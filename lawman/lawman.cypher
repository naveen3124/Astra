LOAD CSV WITH HEADERS FROM 'file:///centralActs.csv' AS row
WITH row WHERE row.Title IS NOT NULL
MERGE (act:LegislativeActs {Title: row.Title, Year: row.Year, Act_No: row.ActNo});


match(n:LegislativeActs) WHERE n.Title =~ 'Police.*' return n;
match(n:LegislativeActs) WHERE n.Title =~ 'Police.*' set n.latestAmendment = 2021;
match(n:LegislativeActs) WHERE n.Title =~ 'Police.*' remove n.latestAmendment;
