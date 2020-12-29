#!/bin/bash
curl "http://localhost:8080/register/love?password=mypassword"; while (true) do for ((users=1;users<=70;users++)) do curl "http://localhost:8080/sendheart/love/$users";done; sleep 0.01; echo "otra"; done;
