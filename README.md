# PayHere Payment Gateway - Integration Boilerplate (Angular + Spring Boot)

This boilerplate provides a complete, scalable, and secure integration of the PayHere Payment Gateway using an Angular (v17+) frontend and a Spring Boot backend.

It is designed with "Separation of Concerns" in mind, ensuring that sensitive data (like Secret Keys) remains secure on the backend while providing a seamless checkout experience on the frontend.

---

# 🏗️ Architecture Overview

## Frontend (Angular)

Displays the UI, collects checkout details, requests a secure payment hash from the backend, and initializes the PayHere JavaScript modal.

## Backend (Spring Boot)

Securely stores PayHere credentials, generates the MD5 Hash required for the transaction, and exposes a Webhook endpoint to receive payment status updates from PayHere.

---

# 🛑 Getting Started: Setup & Testing (Sandbox Mode)

Before deploying to production with real money, you must test the integration using the PayHere Sandbox.

---

# Step 1: Create a PayHere Sandbox Account

Visit the PayHere Sandbox and register for a test account.(https://sandbox.payhere.lk/)

> Note: Sandbox is a completely separate clone of the live platform. No real payments are processed, and you cannot convert a Sandbox account to a Live account.

---

# Step 2: Retrieve Sandbox Credentials

To connect the application, you need your Merchant ID and Merchant Secret.

## Merchant ID

Log in to your Sandbox Dashboard.

Your Merchant ID (a 5 to 7-digit number, e.g., `121XXXX`) is displayed at the top right, under your profile name.

## Merchant Secret

1. Go to **Integrations -> Add Domain/App** in the left menu.
2. Click **"Add new Domain/App"**.
3. Important: In the **"Domain Name"** field, simply type:

```txt
localhost
```

> Do not include `http://` or port numbers like `:4200`.

4. Click Save.
5. A long Secret Key will be generated. Copy this.

---

# Step 3: Configure the Backend (Spring Boot)

Open:

```txt
backend/src/main/resources/application.properties
```

Add the credentials you retrieved in Step 2:

```properties
payhere.merchant.id=YOUR_MERCHANT_ID
payhere.merchant.secret=YOUR_MERCHANT_SECRET
payhere.currency=LKR
```

Run the Spring Boot application (defaults to):

```txt
http://localhost:8080
```

---

# Step 4: Configure Webhooks for Local Testing (ngrok)

PayHere needs a public URL to send payment notifications (Webhooks). Since your backend is running on localhost, you must use a tunneling service like ngrok to expose it to the internet temporarily.

---

# How to Set Up ngrok

## 1. Download & Create Account

- Go to ngrok.com
- Sign up for a free account
- Download the `ngrok.exe` file for your operating system
- Extract the ZIP file

## 2. Authenticate

On the ngrok dashboard, find:

```txt
Your Authtoken
```

Copy the provided command:

```bash
ngrok config add-authtoken <your_token>
```

Open Command Prompt in the folder where `ngrok.exe` is located, paste the command, and press Enter.

## 3. Start Tunnel

Your Spring Boot backend runs on port `8080`.

Run:

```bash
ngrok http 8080
```

## 4. Get the Public URL

A black terminal window will appear showing your tunnel status.

Look for:

```txt
Forwarding
```

Example:

```txt
https://1a2b-3c4d.ngrok-free.app
```

Copy the exact HTTPS URL.

> Keep this terminal window open while testing. Free accounts generate a new URL every time ngrok restarts.

---

# Step 5: Configure the Frontend (Angular)

Open:

```txt
frontend/src/app/components/checkout/checkout.component.ts
```

Locate the payment object inside the `initiatePayment()` method.

Update the `notify_url` with your ngrok URL:

```javascript
"notify_url": "https://YOUR-NGROK-URL/api/v1/payments/webhook"
```

Run the Angular application:

```bash
cd frontend
ng serve -o
```

Navigate to:

```txt
http://localhost:4200/checkout
```

---

# 💳 Testing Sandbox Payments

Click the **"Pay with PayHere"** button to open the modal.

Use the following test card numbers provided by PayHere to simulate different scenarios.

> Note: For `Name on Card`, `CVV`, and `Expiry date`, you can enter any valid dummy data.

---

# ✅ Successful Payments

| Card Type | Card Number |
|---|---|
| Visa | 4916217501611292 |
| MasterCard | 5307732125531191 |
| AMEX | 346781005510225 |

---

# ❌ Decline Scenarios

## Insufficient Funds

| Card Type | Card Number |
|---|---|
| Visa | 4024007194349121 |
| MasterCard | 5459051433777487 |
| AMEX | 370787711978928 |

## Limit Exceeded

| Card Type | Card Number |
|---|---|
| Visa | 4929119799365646 |
| MasterCard | 5491182243178283 |
| AMEX | 340701811823469 |

## Do Not Honor

| Card Type | Card Number |
|---|---|
| Visa | 4929768900837248 |
| MasterCard | 5388172137367973 |
| AMEX | 374664175202812 |

## Network Error

| Card Type | Card Number |
|---|---|
| Visa | 4024007120869333 |
| MasterCard | 5237980565185003 |
| AMEX | 373433500205887 |

> Any card except the above test cards will result in a failed payment in the Sandbox.

---

# 🚀 Moving to LIVE (Production)

Once you have successfully tested the flow and are ready to process real transactions:

## 1. Apply for a Live Account

Register at:

```txt
www.payhere.lk
```

Submit your business and bank details for approval.

---

## 2. Get Live Credentials

Once approved:

- Retrieve your Live Merchant ID
- Add your Live Domain (e.g., `yourdomain.com`)
- Generate your Live Merchant Secret

---

## 3. Update Backend

Update:

```properties
application.properties
```

Use your Live Merchant ID and Secret.

---

## 4. Update Frontend (`checkout.component.ts`)

Change:

```javascript
"sandbox": true
```

to:

```javascript
"sandbox": false
```

Also update:

- `return_url`
- `cancel_url`
- `notify_url`

Use your actual production backend URL instead of ngrok.

---

## 5. Dynamic Data

Replace hardcoded:

- Customer Name
- Email
- Address
- Order Amount
- Items

with dynamic data collected from your application's UI.

---

# 🛡️ Security Best Practices

- Never commit your `application.properties` file with live secret keys to a public repository.
- Use environment variables in production.
- The backend webhook endpoint (`/webhook`) must always verify the `md5sig` parameter against your Merchant Secret before updating database records to prevent spoofed requests.