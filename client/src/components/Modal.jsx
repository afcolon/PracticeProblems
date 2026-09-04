import { useEffect, useRef } from "react";
import styled from "styled-components";
import { X } from "lucide-react";

const StyledDialog = styled.dialog`
    background: var(--color-surface);
    color: var(--color-text);
    border: 1px solid var(--color-border);
    border-radius: var(--radius-lg);
    padding: var(--space-lg);
    max-width: 400px;

    &::backdrop {
        background: rgba(0, 0, 0, 0.5);
    }
`;

const CloseButton = styled.button`
    background: transparent;
    border: none;
    color: var(--color-text-muted);
    cursor: pointer;
    float: right;

    &:hover {
        color: var(--color-text);
    }
`

export default function Modal({ isOpen, onClose, children }) {
    const dialogRef = useRef(null);

    useEffect(() => {
        const dialog = dialogRef.current;
        if (!dialog) return;

        if (isOpen) {
            dialog.showModal();
        } else {
            dialog.close();
        }
    }, [isOpen]);

    // Handle closure when user presses the Esc key
    const handleCancel = (e) => {
        e.preventDefault();
        onClose();
    };

    return (
        <StyledDialog ref={dialogRef} onCancel={handleCancel}>
            <CloseButton onClick={onClose}><X/></CloseButton>
            {children}
        </StyledDialog>
    );
}
