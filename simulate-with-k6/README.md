Test that k6 is working: `k6 run test-k6.js`

Run k6 for marketplace:
```shell
k6 run \
  -e mp_auth_token=%auth_token_here% \
  -e mp_server_url=%mp_server_url_here% \
  --config options.json \
  --quiet \
  test.js
```
