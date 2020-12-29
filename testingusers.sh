#!/bin/bash
curl "http://localhost:8080/register/love?password=mypassword"; while (true) do for ((users=1;users<=500;users++)) do curl "http://localhost:8080/sendheart/love/$users";done; echo "terminado"; sleep 0.01; done;
