CREATE CONSTRAINT IF NOT EXISTS ON (act:LegislativeAct) ASSERT act.title IS UNIQUE;
UNWIND [ { title: "Indian Evidence Act",
           enacted: date("1872-03-15"),
           commenced: date("1872-09-01") },
         { title: "Indian Penal Code",
           enacted: date("1860-10-06"),
           commenced: date("1862-01-01") }
        ] as legislativeActProperties
CREATE (act:LegislativeAct {title: legislativeActProperties.title})
SET act.enacted = legislativeActProperties.enacted,
    act.commenced = legislativeActProperties.commenced;



