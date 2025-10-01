/**
 * NextGen Facade Payment Gateway JavaScript
 * Handles Razorpay integration, UPI payments, and form interactions
 */

class PaymentGateway {
  constructor() {
    this.init();
    this.setupEventListeners();
  }

  // Configuration - Replace with your actual keys
  config = {
    razorpayKeyId: "rzp_test_xxxxxxxxxxxx", // Replace with your key
    merchantUPI: "nextgenfacade@upi",        // Replace with your UPI ID
    companyName: "NextGen Facade",
    currency: "INR"
  };

  // Plan configurations
  plans = {
    starter: {
      name: "Starter Plan",
      amount: 79900, // in paise (₹799)
      features: ["10 calculation templates/month", "Basic load analysis", "Email support"]
    },
    professional: {
      name: "Professional Plan",
      amount: 149900, // in paise (₹1,499)
      features: ["Unlimited templates", "Advanced wind analysis", "Priority support"]
    },
    enterprise: {
      name: "Enterprise Plan",
      amount: 499900, // in paise (₹4,999)
      features: ["10 team seats", "Custom modules", "Dedicated engineer support"]
    }
  };

  // Initialize the application
  init() {
    this.setCurrentYear();
    this.validateRazorpaySDK();
    this.addLoadingStates();
  }

  // Set current year in footer
  setCurrentYear() {
    const yearElement = document.getElementById('current-year');
    if (yearElement) {
      yearElement.textContent = new Date().getFullYear();
    }
  }

  // Validate if Razorpay SDK is loaded
  validateRazorpaySDK() {
    if (typeof Razorpay === 'undefined') {
      console.error('Razorpay SDK not loaded. Please check your internet connection.');
      this.showError('Payment system not available. Please refresh the page.');
    }
  }

  // Setup event listeners
  setupEventListeners() {
    // Add smooth scrolling for internal links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
      anchor.addEventListener('click', (e) => {
        e.preventDefault();
        const target = document.querySelector(anchor.getAttribute('href'));
        if (target) {
          target.scrollIntoView({ behavior: 'smooth' });
        }
      });
    });

    // Add keyboard navigation for plan cards
    document.querySelectorAll('.plan-card').forEach(card => {
      card.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          const primaryBtn = card.querySelector('.btn-primary');
          if (primaryBtn) primaryBtn.click();
        }
      });
    });
  }

  // Main payment initiation function
  async initiatePayment(planType, amount) {
    try {
      this.showLoading(event.target);

      // Validate plan
      if (!this.plans[planType]) {
        throw new Error('Invalid plan selected');
      }

      // In production, create order on server
      // const orderData = await this.createServerOrder(planType, amount);

      const options = this.buildRazorpayOptions(planType, amount);
      const rzp = new Razorpay(options);

      // Open Razorpay checkout
      rzp.open();

    } catch (error) {
      this.handlePaymentError(error);
    } finally {
      this.hideLoading(event.target);
    }
  }

  // Build Razorpay options object
  buildRazorpayOptions(planType, amount) {
    const plan = this.plans[planType];

    return {
      key: this.config.razorpayKeyId,
      amount: amount,
      currency: this.config.currency,
      name: this.config.companyName,
      description: plan.name,
      image: this.getCompanyLogo(),

      // Order ID - implement server-side order creation
      // order_id: orderData.id,

      // Prefill customer information
      prefill: this.getCustomerPrefill(),

      // Payment notes
      notes: {
        plan_type: planType,
        plan_name: plan.name,
        timestamp: new Date().toISOString()
      },

      // Theme customization
      theme: {
        color: "#12DAD1",
        backdrop_color: "rgba(14, 17, 22, 0.8)"
      },

      // Modal configuration
      modal: {
        ondismiss: () => this.handlePaymentDismiss(),
        confirm_close: true,
        escape: true,
        animation: true
      },

      // Success handler
      handler: (response) => this.handlePaymentSuccess(response, planType),

      // Supported payment methods
      method: {
        netbanking: true,
        card: true,
        upi: true,
        wallet: true,
        emi: true,
        paylater: true
      },

      // UPI configuration
      config: {
        display: {
          blocks: {
            banks: {
              name: "Most Used",
              instruments: [
                { method: "upi", flows: ["collect"], apps: ["google_pay"] },
                { method: "upi", flows: ["collect"], apps: ["paytm"] },
                { method: "upi", flows: ["collect"], apps: ["phonepe"] }
              ]
            }
          },
          sequence: ["block.banks"],
          preferences: {
            show_default_blocks: true
          }
        }
      }
    };
  }

  // Get customer prefill data (can be enhanced with user data)
  getCustomerPrefill() {
    return {
      name: "",
      email: "",
      contact: "",
      method: "upi" // Default to UPI
    };
  }

  // Get company logo URL
  getCompanyLogo() {
    // Return actual logo URL or base64 encoded image
    return "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0iIzEyREFEMSI+PHBhdGggZD0iTTMgM2gxOHYySDNWM3ptMiA0aDE0djJINVY3em0tMiA0aDE4djJIM3YtMnptMiA0aDE0djJINXYtMnoiLz48L3N2Zz4=";
  }

  // Handle successful payment
  handlePaymentSuccess(response, planType) {
    console.log('Payment successful:', response);

    // Extract payment details
    const paymentData = {
      paymentId: response.razorpay_payment_id,
      orderId: response.razorpay_order_id,
      signature: response.razorpay_signature,
      planType: planType,
      timestamp: new Date().toISOString()
    };

    // Send to server for verification
    this.verifyPaymentOnServer(paymentData)
      .then((verification) => {
        if (verification.success) {
          this.showPaymentSuccess(planType);
          // Redirect or show success UI
          setTimeout(() => {
            window.location.href = '/dashboard'; // Replace with actual redirect
          }, 3000);
        } else {
          this.showError('Payment verification failed. Please contact support.');
        }
      })
      .catch((error) => {
        console.error('Verification error:', error);
        this.showError('Payment verification failed. Please contact support.');
      });
  }

  // Verify payment on server (implement this endpoint)
  async verifyPaymentOnServer(paymentData) {
    try {
      const response = await fetch('/api/verify-payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-Token': this.getCSRFToken()
        },
        body: JSON.stringify(paymentData)
      });

      return await response.json();
    } catch (error) {
      throw new Error('Server verification failed');
    }
  }

  // Get CSRF token for security
  getCSRFToken() {
    const token = document.querySelector('meta[name="csrf-token"]');
    return token ? token.getAttribute('content') : '';
  }

  // Handle payment dismissal
  handlePaymentDismiss() {
    console.log('Payment dialog dismissed');
    this.showNotification('Payment cancelled', 'info');
  }

  // Handle payment errors
  handlePaymentError(error) {
    console.error('Payment error:', error);

    let errorMessage = 'Payment failed. Please try again.';

    if (error.code === 'BAD_REQUEST_ERROR') {
      errorMessage = 'Invalid payment details. Please check and try again.';
    } else if (error.code === 'GATEWAY_ERROR') {
      errorMessage = 'Payment gateway error. Please try a different method.';
    } else if (error.code === 'NETWORK_ERROR') {
      errorMessage = 'Network error. Please check your connection.';
    }

    this.showError(errorMessage);
  }

  // Open UPI payment intent (direct UPI apps)
  openUPIPayment(amount, planName) {
    if (!this.config.merchantUPI || this.config.merchantUPI.includes('nextgenfacade@upi')) {
      this.showError('UPI payment not configured. Please use Razorpay checkout.');
      return;
    }

    const amountInRupees = (amount / 100).toFixed(2);
    const transactionNote = encodeURIComponent(`${planName} - ${this.config.companyName}`);
    const merchantName = encodeURIComponent(this.config.companyName);

    // UPI deep link format
    const upiUrl = `upi://pay?pa=${this.config.merchantUPI}&pn=${merchantName}&am=${amountInRupees}&cu=INR&tn=${transactionNote}`;

    // Try to open UPI app
    try {
      window.location.href = upiUrl;

      // Show fallback message after 3 seconds
      setTimeout(() => {
        this.showNotification('If UPI app didn\'t open, please use the "Subscribe Now" button', 'info');
      }, 3000);
    } catch (error) {
      this.showError('Could not open UPI app. Please use Razorpay checkout.');
    }
  }

  // Create server order (implement this endpoint)
  async createServerOrder(planType, amount) {
    try {
      const response = await fetch('/api/create-order', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-Token': this.getCSRFToken()
        },
        body: JSON.stringify({
          plan_type: planType,
          amount: amount,
          currency: this.config.currency
        })
      });

      if (!response.ok) {
        throw new Error('Failed to create order');
      }

      return await response.json();
    } catch (error) {
      throw new Error('Server order creation failed');
    }
  }

  // UI Helper Methods
  showLoading(button) {
    if (button) {
      button.classList.add('loading');
      button.disabled = true;
    }
  }

  hideLoading(button) {
    if (button) {
      button.classList.remove('loading');
      button.disabled = false;
    }
  }

  showPaymentSuccess(planType) {
    const plan = this.plans[planType];
    this.showNotification(`🎉 Successfully subscribed to ${plan.name}!`, 'success');
  }

  showError(message) {
    this.showNotification(message, 'error');
  }

  showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
      <div class="notification-content">
        <span>${message}</span>
        <button onclick="this.parentElement.parentElement.remove()">×</button>
      </div>
    `;

    // Add styles for notification
    const style = document.createElement('style');
    style.textContent = `
      .notification {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 1000;
        max-width: 400px;
        padding: 16px;
        border-radius: 12px;
        box-shadow: var(--shadow-lg);
        animation: slideIn 0.3s ease;
      }
      .notification-success { background: var(--success); color: white; }
      .notification-error { background: var(--error); color: white; }
      .notification-info { background: var(--brand-teal); color: var(--bg-primary); }
      .notification-content { display: flex; justify-content: space-between; align-items: center; }
      .notification button { background: none; border: none; color: inherit; font-size: 18px; cursor: pointer; }
      @keyframes slideIn { from { transform: translateX(100%); } to { transform: translateX(0); } }
    `;

    if (!document.querySelector('#notification-styles')) {
      style.id = 'notification-styles';
      document.head.appendChild(style);
    }

    // Add to DOM
    document.body.appendChild(notification);

    // Auto remove after 5 seconds
    setTimeout(() => {
      if (notification.parentElement) {
        notification.remove();
      }
    }, 5000);
  }

  // Add loading states to buttons
  addLoadingStates() {
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary');
    buttons.forEach(button => {
      button.addEventListener('click', (e) => {
        // Add loading state briefly to show interaction
        e.target.style.opacity = '0.7';
        setTimeout(() => {
          e.target.style.opacity = '1';
        }, 200);
      });
    });
  }
}

// Initialize payment gateway when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
  const paymentGateway = new PaymentGateway();

  // Make functions globally available for onclick handlers
  window.initiatePayment = (planType, amount) => paymentGateway.initiatePayment(planType, amount);
  window.openUPIPayment = (amount, planName) => paymentGateway.openUPIPayment(amount, planName);
});

// Analytics tracking (optional)
function trackPaymentEvent(eventName, planType, amount) {
  // Google Analytics 4
  if (typeof gtag !== 'undefined') {
    gtag('event', eventName, {
      event_category: 'Payment',
      event_label: planType,
      value: amount / 100 // convert to rupees
    });
  }

  // Facebook Pixel
  if (typeof fbq !== 'undefined') {
    fbq('track', 'InitiateCheckout', {
      content_name: planType,
      value: amount / 100,
      currency: 'INR'
    });
  }
}