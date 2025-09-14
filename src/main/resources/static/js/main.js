
    // Toggle Window dropdown
    function toggleDropdown(menuId) {
            document.querySelectorAll('.dropdown-menu').forEach(menu => menu.classList.add('hidden'));

            // Toggle the requested one
            const dropdown = document.getElementById(menuId);
            dropdown.classList.toggle('hidden');
    }

    // Show window flow content
    function showWindowFlow(type) {
        hideAllContent();
        document.getElementById('window-flow-content').classList.remove('hidden');

        if (type === 'sliding') {
            document.getElementById('sliding-flow').classList.remove('hidden');
            document.getElementById('casement-flow').classList.add('hidden');
        } else {
            document.getElementById('casement-flow').classList.remove('hidden');
            document.getElementById('sliding-flow').classList.add('hidden');
        }
    }

    function setActiveButtonForSideBarMenu(button) {
        // Remove active from all sidebar buttons (not dropdowns)
         document.querySelectorAll('.sidebar-btn').forEach(btn => btn.classList.remove('active'));
            // Remove active from ALL children
            document.querySelectorAll('.dropdown-menu button').forEach(btn => btn.classList.remove('active'));

            if (!button.classList.contains('window-btn')) {
                // If Semi/Fully clicked → collapse Window dropdown
                document.getElementById('window-dropdown').classList.add('hidden');
            }

            // Always set active on clicked parent
            button.classList.add('active');
    }

    function setActiveButtonForToggleLeftSideBarMenu(button) {
        // Remove 'active' from all toggle buttons inside window-dropdown
           const toggleBtns = document.querySelectorAll('#window-dropdown .leftSidebar-toggle-btn');
           toggleBtns.forEach(btn => btn.classList.remove('active'));

           // Add 'active' to the clicked one
           button.classList.add('active');
    }

    // Show flow steps
    function showFlowStep(windowType, flowType) {
        const stepsContainer = document.getElementById(windowType + '-flow-steps');
        let content = '';

        if (flowType === 'combined') {
            content = `
                    <div class="flow-step">
                        <h4>Step 1: Combined Form</h4>
                        <p>Configure combined form parameters...</p>
                        <form class="flow-form">
                            <input type="text" placeholder="Parameter 1" required>
                            <input type="text" placeholder="Parameter 2" required>
                            <button type="button" onclick="nextFlowStep('${windowType}', 'central')">Next: Central Meeting</button>
                        </form>
                    </div>
                `;
        } else {
            content = `
                    <div class="flow-step">
                        <h4>Step 1: A+B Form</h4>
                        <p>Configure A+B form parameters...</p>
                        <form class="flow-form">
                            <input type="text" placeholder="Parameter A" required>
                            <input type="text" placeholder="Parameter B" required>
                            <button type="button" onclick="nextFlowStep('${windowType}', 'central')">Next: Central Meeting</button>
                        </form>
                    </div>
                `;
        }
        stepsContainer.innerHTML = content;
    }

    // Navigate to next flow step
    function nextFlowStep(windowType, step) {
        const stepsContainer = document.getElementById(windowType + '-flow-steps');
        let content = '';

        if (step === 'central') {
            content = `
                    <div class="flow-step">
                        <h4>Step 2: Central Meeting</h4>
                        <p>Configure central meeting parameters...</p>
                        <form class="flow-form">
                            <input type="text" placeholder="Central Parameter 1" required>
                            <input type="text" placeholder="Central Parameter 2" required>
                            <button type="button" onclick="nextFlowStep('${windowType}', 'outer')">Next: Outer</button>
                        </form>
                    </div>
                `;
        } else if (step === 'outer') {
            content = `
                    <div class="flow-step">
                        <h4>Step 3: Outer Configuration</h4>
                        <p>Final outer configuration...</p>
                        <form class="flow-form">
                            <input type="text" placeholder="Outer Parameter 1" required>
                            <input type="text" placeholder="Outer Parameter 2" required>
                            <button type="submit">Complete Flow</button>
                        </form>
                    </div>
                `;
        }
        stepsContainer.innerHTML = content;
        updateHistory(`${windowType} - ${step} step configured`);
    }

    // Show main forms (Semi/Fully Unitized)
    function showForm(type) {
        hideAllContent();

        if (type === 'semi-unitized') {
            document.getElementById('semi-form').classList.remove('hidden');
        } else if (type === 'fully-unitized') {
            document.getElementById('fully-form').classList.remove('hidden');
        }
    }


    // Hide all content sections
    function hideAllContent() {
        document.getElementById('default-content').classList.add('hidden');
        document.getElementById('window-flow-content').classList.add('hidden');
        document.getElementById('semi-form').classList.add('hidden');
        document.getElementById('fully-form').classList.add('hidden');
        document.getElementById('sliding-flow').classList.add('hidden');
        document.getElementById('casement-flow').classList.add('hidden');
    }

    // Update history (placeholder function)
    function updateHistory(action) {
        // This would typically update the right sidebar history
        console.log('History updated:', action);
    }

    // Initialize page
    window.onload = function() {
        // Check if there are existing results to show a form
        const hasResults = document.getElementById('results-marker') !== null;
            if (!hasResults) {
                document.getElementById('default-content').classList.remove('hidden');
            } else {
                // If results exist, show sliding window form (or whichever was last submitted)
                document.getElementById('window-flow-content').classList.remove('hidden');
                document.getElementById('sliding-flow').classList.remove('hidden');
            }
    };
